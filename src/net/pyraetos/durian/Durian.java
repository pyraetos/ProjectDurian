package net.pyraetos.durian;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JApplet;
import javax.swing.JPanel;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;
import net.pyraetos.durian.server.Packet;
import net.pyraetos.durian.server.PacketAppletQuit;
import net.pyraetos.durian.server.PacketTileRequest;
import net.pyraetos.util.Config;
import net.pyraetos.util.Images;
import net.pyraetos.util.Point;
import net.pyraetos.util.Sounds;
import net.pyraetos.util.Sys;

@SuppressWarnings("serial")
public class Durian extends JPanel implements Runnable{

	//instance fields ya dig
	private static Durian instance;
	
	//window fields
	private static boolean applet;
	private static double screenX;
	private static double screenY;
	private static double gameWidth;
	private static double gameHeight;
	private static Container container;
	
	//game fields
	private static Config config;
	private static Player player;
	private static boolean ready;
	private static boolean running;
	
	//server fields
	private static Socket server;
	private static String serverHostName;
	private static int serverPort;
	private static Queue<Packet> queue;
	private static Set<Point> requestedTiles;
	
	/*
	 * ProjectDurian TODO List
	 * ***********************
	 * 
	 * add chat
	 * add saving of tileset
	 * add a way to cancel threads
	 */
	
	public Durian(Container container){
		instance = this;
		Status.set("Loading...");
		applet = container instanceof JApplet;
		Durian.container = container;
		int width = applet ? 680 : DurianFrame.FRAME_WIDTH;
		int height = applet ? 680 : DurianFrame.FRAME_HEIGHT;
		setFocusable(true);
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(width, height-95));
		screenX = -5;
		screenY = -5;
		gameWidth = (double)width / 50d;
		gameHeight = (double)height / 50d + 1d;
		running = true;
		container.addKeyListener(new PyroKeyAdapter());
		container.requestFocus();
		boolean multiplayer;
		if(!applet){
			config = new Config("config.txt");
			Images.fromURL(config.getString("imagesURL", "http://www.pyraetos.net/images"));
			Sounds.fromURL(config.getString("soundsURL", "http://www.pyraetos.net/sounds"));
			multiplayer = config.getBoolean("multiplayer", false);
			serverHostName = config.getString("serverHostName", "pyraetos.net");
			serverPort = config.getInt("serverPort", 1337);
		}else{
			Images.fromPyraetosNet();
			Sounds.fromPyraetosNet();
			multiplayer = true;
			serverHostName = "pyraetos.net";
			serverPort = 1337;
		}
		Sys.thread(this);
		if(multiplayer)
			Sys.thread(new ConnectThread());
		else
			playOffline();
	}
	
	private static void playOffline(){
		if(applet){
			Status.set("Offline mode is not available in applet.");
			return;
		}
		Tileset.setSeed(parseSeed());
		Tileset.setEntropy(Math.abs(config.getDouble("entropy", 1.0)));
		config.comment("entropy", "Does not affect seed. Uses absolute value.");
		player = new Player(0, 0, true);
		int side = config.getInt("generate", 32);
		config.comment("generate", "Generates a square of tiles at startup with this value for the side length");
		for(int x = -side/2; x < side/2; x++){
			for(int y = -side/2; y < side/2; y++){
				Tileset.pgenerate(x, y);
			}
		}
		ready = true;
		Status.set("Ready!");
		if(!applet)
			DurianFrame.modifyTitle(DurianFrame.OFFLINE);
	}
	
	private static long parseSeed(){
		String s = config.getString("seed", "random");
		config.comment("seed", "Does not affect entropy. Special values: random");
		if(!s.equalsIgnoreCase("random"))
		try{
			return Long.parseLong(s);
		}catch(Exception e){
			return s.hashCode();
		}
		else return Sys.randomSeed();
	}

	static void setGameSize(int width, int height){
		instance.setSize(width, height);
		gameHeight = ((double)height) / 50d + 1d;
		gameWidth = ((double)width) / 50d;
		container.revalidate();
	}
	
	@Override
	public void run(){
		while(running){
			repaint();
			Sys.sleep(5);
		}
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		container.requestFocus();
		if(ready){
			drawTileset(g);
			drawEntities(g);
		}
		g.dispose();
	}
	
	public static boolean isOnline(){
		return server != null;
	}
	
	private void drawTileset(Graphics g){
		for(int x = (int)screenX - 1; x <= (int)screenX + gameWidth + 6; x++){
			for(int y = (int)screenY - 1; y <= (int)screenY + gameHeight + 6; y++){
				Point point = new Point(x, y);
				byte type = Tileset.getTile(point);
				if(type == Tileset.NULL){
					if(isOnline()){
						if(!requestedTiles.contains(point)){
							send(new PacketTileRequest(x, y));
							requestedTiles.add(point);
						}
					}else{
						Tileset.pgenerate(x, y);
						type = Tileset.getTile(x, y);
					}
				}
				g.drawImage(Tileset.imageFor(type), (int)((x - screenX) * 50), (int)((y - screenY) * 50), null);
			}
		}
	}

	private void drawEntities(Graphics g){
		for(Entity entity : Entity.getEntities()){
			double x = entity.getX();
			double y = entity.getY();
			if(isOnScreen(x, y)){
				g.drawImage(entity.getSprite(), (int)((x - screenX) * 50), (int)((y - screenY) * 50), null);
			}
		}
	}

	public static void setPlayer(Player player){
		if(Durian.player != null)
			Entity.removeEntity(Durian.player);
		Durian.player = player;
		player.setFocused(true);
		ready = true;
		Status.set("Ready!");
		if(!applet)
			DurianFrame.modifyTitle(DurianFrame.ONLINE);
	}
	
	public synchronized static void send(Packet packet){
		if(isOnline())
			queue.add(packet);
	}

	public static void handle(Packet packet){
		packet.process();
	}

	public static void kill(){
		if(isOnline()){
			Durian.send(new PacketAppletQuit());
			disconnect();
		}
		running = false;
	}
	
	public static void disconnect(){
		try{
			server.close();
		}catch(IOException e){}
		server = null;
		queue = null;
		Status.set("Disconnected from " + serverHostName + "!");
		if(applet)
			kill();
		else
			DurianFrame.modifyTitle(DurianFrame.OFFLINE);
	}

	public static double getScreenX(){
		return screenX;
	}

	public static double getScreenY(){
		return screenY;
	}

	public static boolean isOnScreen(double x, double y){
		double xx = Sys.round(x - screenX);
		double yy = Sys.round(y - screenY);
		if(xx < 0 || xx >= gameWidth) return false;
		if(yy < 0 || yy >= gameHeight) return false;
		return true;
	}

	public static boolean isOnScreen(Entity entity){
		return isOnScreen(entity.getX(), entity.getY());
	}

	public static void moveScreen(double magnitude, byte direction){
		double dx = 0;
		double dy = 0;
		switch(direction){
		case Sys.NORTH: dy = -magnitude; break;
		case Sys.WEST: dx = -magnitude; break;
		case Sys.SOUTH: dy = magnitude; break;
		case Sys.EAST: dx = magnitude; break;
		}
		for(int i = 0; i < 50; i++){
			screenX = Sys.round(screenX + dx / 50);
			screenY = Sys.round(screenY + dy / 50);
			Sys.sleep(5);
		}
	}

	public static void setScreen(double x, double y){
		screenX = x;
		screenY = y;
	}

	private static class PyroKeyAdapter extends KeyAdapter implements Runnable{

		private Set<Integer> keysDown;

		public PyroKeyAdapter(){
			keysDown = new HashSet<Integer>();
			Sys.thread(this);
		}

		public void keyPressed(KeyEvent event){
			if(!ready) return;
			keysDown.add(event.getKeyCode());
		}	

		public void keyReleased(KeyEvent event){
			if(!ready) return;
			keysDown.remove(event.getKeyCode());
		}

		@Override
		public void run(){
			boolean acted = false;
			while(running){
				if(ready){
					if(!player.isMoving()){
						if(keysDown.contains(KeyEvent.VK_W)){
							player.move(1, Sys.NORTH);
						}
						if(keysDown.contains(KeyEvent.VK_A)){
							player.move(1, Sys.WEST);
						}
						if(keysDown.contains(KeyEvent.VK_S)){
							player.move(1, Sys.SOUTH);
						}
						if(keysDown.contains(KeyEvent.VK_D)){
							player.move(1, Sys.EAST);
						}
					}
					if(keysDown.contains(KeyEvent.VK_SPACE)){
						if(!acted){
							player.cut();
							acted = true;
						}
					}else acted = false;
				}
				Sys.sleep(5);
			}
		}
	}

	private static class ConnectThread implements Runnable{
		@Override
		public void run(){
			try{
				Status.set("Trying to connect to server " + serverHostName + ":" + serverPort + "...");
				SocketAddress address = new InetSocketAddress(serverHostName, serverPort);
				server = new Socket();
				server.connect(address, 3000);
				queue = new LinkedList<Packet>();
				requestedTiles = new HashSet<Point>();
				Sys.thread(new InputThread());
				Sys.thread(new OutputThread());
			}catch(Exception e){
				e.printStackTrace();
				disconnect();
				playOffline();
			}
		}
	}

	private static class InputThread implements Runnable{
		private ObjectInputStream in;
		{
			try{
				in = new ObjectInputStream(server.getInputStream());
			} catch(IOException e){
				e.printStackTrace();
			}
		}

		@Override
		public void run(){
			try{
				while(true){
					handle((Packet)in.readObject());
					Sys.sleep(5);
				}
			}catch(Exception e){
				if(server != null)
					disconnect();
			}
		}
	}

	private static class OutputThread implements Runnable{
		private ObjectOutputStream out;
		{
			try{
				out = new ObjectOutputStream(server.getOutputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
		}		
		@Override
		public void run(){
			try{
				while(true){
					if(!queue.isEmpty()){
						out.writeObject(queue.poll());
						out.flush();
					}
					Sys.sleep(5);
				}
			}catch(Exception e){
				if(server != null)
					disconnect();
			}
		}
	}

}