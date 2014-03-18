package net.pyraetos.durian;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JPanel;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;
import net.pyraetos.durian.server.Packet;
import net.pyraetos.durian.server.PacketJoinMe;
import net.pyraetos.util.Config;
import net.pyraetos.util.Sys;

public class Durian extends JPanel implements Runnable{

	//window fields
	private static double screenX;
	private static double screenY;
	private static double gameWidth;
	private static double gameHeight;
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 850;
	
	//game fields
	private static int nextEntityUID;
	private static Config config;
	private static AttributedCharacterIterator status;
	private static Player player;
	
	//server fields
	private static Socket server;
	private static String serverHostName;
	private static int serverPort;
	private static Queue<Packet> queue;
	
	
	public Durian(Container container){
		setFocusable(true);
		setDoubleBuffered(true);
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		screenX = 0;
		screenY = 0;
		gameWidth = FRAME_WIDTH / 50;
		gameHeight = FRAME_HEIGHT / 50 - 1;
		config = new Config("config.txt");
		container.addKeyListener(new PyroKeyAdapter());
		setStatus("Loading...");
		Sys.thread(this);
		boolean multiplayer = config.getBoolean("multiplayer", false);
		serverHostName = config.getString("serverHostName", "pyraetos.net");
		serverPort = config.getInt("serverPort", 1337);
		if(multiplayer)
			Sys.thread(new ConnectThread());
		else
			playOffline();
	}
	
	private static void playOffline(){
		Tileset.generate(0, 0);
		player = new Player(5, 5);
		player.assign(nextEntityUID++);
		Entity.addEntity(player);
		setStatus("Playing offline!");
	}
	
	@Override
	public void run(){
		while(true){
			repaint();
			Sys.sleep(5);
		}
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(ready()){
			drawTileset(g);
			drawEntities(g);
		}
		drawStatus(g);
		g.dispose();
	}
	
	public static boolean online(){
		return server != null;
	}
	
	public static boolean ready(){
		return Tileset.ready() && player != null;
	}

	private void drawStatus(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, (int)gameHeight * 50, FRAME_WIDTH, 50);
		g.setColor(Color.WHITE);
		g.drawString(status, 10, (int)gameHeight * 50 + 30);
	}

	private void drawTileset(Graphics g){
		for(int x = (int)screenX - 1; x <= (int)screenX + gameWidth; x++){
			for(int y = (int)screenY - 1; y <= (int)screenY + gameHeight; y++){
				byte type = Tileset.getTile(x, y);
				if(type == Tileset.NULL){
					Tileset.generate(Tileset.toRegionCoordinate(x), Tileset.toRegionCoordinate(y));
					type = Tileset.getTile(x, y);
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

	public static void setStatus(String s){
		AttributedString string = new AttributedString(s);
		string.addAttribute(TextAttribute.FONT, new Font("Courier New", Font.PLAIN, 16));
		status = string.getIterator();
	}

	public static void send(Packet packet){
		if(online())
		queue.add(packet);
	}

	public static void handle(Packet packet){
		if(packet instanceof PacketJoinMe){
			packet.process();
			player = (Player)Entity.getEntity(((PacketJoinMe)packet).getUID());
		}else
		packet.process();
	}

	public static void disconnect(){
		try{
			server.close();
		}catch(IOException e){}
		server = null;
		queue = null;
		setStatus("Disconnected from " + serverHostName + "! Playing offline!");
		return;
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

	private static class PyroKeyAdapter extends KeyAdapter implements Runnable{
		
		private Set<Integer> keysDown;
		
		public PyroKeyAdapter(){
			keysDown = new HashSet<Integer>();
			Sys.thread(this);
		}
		
		public void keyPressed(KeyEvent event){
			if(!ready()) return;
			keysDown.add(event.getKeyCode());
		}	

		public void keyReleased(KeyEvent event){
			if(!ready()) return;
			keysDown.remove(event.getKeyCode());
		}

		@Override
		public void run(){
			while(true){
				if(ready()){
					if(!player.isMoving()){
						if(keysDown.contains(KeyEvent.VK_W)){
							player.move(Sys.NORTH);
						}
						if(keysDown.contains(KeyEvent.VK_A)){
							player.move(Sys.WEST);
						}
						if(keysDown.contains(KeyEvent.VK_S)){
							player.move(Sys.SOUTH);
						}
						if(keysDown.contains(KeyEvent.VK_D)){
							player.move(Sys.EAST);
						}
					}
				}
				Sys.sleep(5);
			}
		}
	}

	private static class ConnectThread implements Runnable{
		@Override
		public void run(){
			try{
				setStatus("Trying to connect to server " + serverHostName + ":" + serverPort + "...");
				SocketAddress address = new InetSocketAddress(serverHostName, serverPort);
				server = new Socket();
				server.connect(address, 3000);
				queue = new LinkedList<Packet>();
				Sys.thread(new InputThread());
				Sys.thread(new OutputThread());
			}catch(Exception e){
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
					setStatus("Done!");
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