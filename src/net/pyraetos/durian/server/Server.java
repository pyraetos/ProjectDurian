package net.pyraetos.durian.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import net.pyraetos.durian.Tileset;
import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;
import net.pyraetos.util.Config;
import net.pyraetos.util.Sys;

public class Server extends ServerSocket implements Runnable{

	private static Set<Connection> connections;
	private static Config config;
	
	public static void start(){
		try{
			config = new Config("serverConfig.txt");
			new Server(config.getInt("port", 1337));
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private Server(int port) throws IOException{
		super(port);
		System.out.println("Durian server started on port " + port + "!");
		connections = new HashSet<Connection>();
		long seed = parseSeed();
		double s = Math.abs(config.getDouble("entropy", 1.0));
		config.comment("entropy", "Does not affect seed. Uses absolute value.");
		Tileset.setSeed(seed);
		Tileset.setEntropy(s);
		System.out.println("Server online! Tileset seed = " + seed + "; Tileset entropy = " + s);
		Sys.thread(this);
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
	
	public static void sendAll(Packet packet){
		for(Connection connection : connections){
			connection.send(packet);
		}
	}
	
	private static void handle(Connection connection, Packet packet){
		if(packet instanceof PacketTileRequest){
			connection.send(((PacketTileRequest)packet).getPacketTile());
		}else if(packet instanceof PacketAppletQuit){
			disconnect(connection);
		}else{
			packet.process();
			for(Connection c : connections){
				if(c != connection){
					c.send(packet);
				}
			}
		}
	}
	
	private static void disconnect(Connection connection){
		connections.remove(connection);
		connection.close();
		int uid = connection.getPlayerUID();
		Entity.removeEntity(uid);
		for(Connection c : connections)
			c.send(new PacketLeave(uid));	
		System.out.println(connection.getSocket().getInetAddress() + " has disconnected from the server!");	
	}
	
	@Override
	public void run(){
		server: while(true){
			try{
				Connection connection = new Connection(accept());
				String ip = connection.getSocket().getInetAddress().toString();
				//check to see if this IP is already connected
				for(Connection c : connections){
					if(c.getSocket().getInetAddress().toString().equals(ip)){
						System.out.println(ip + " is trying to connect but is already connected!");
						connection.close();
						continue server;
					}
				}
				System.out.println(ip + " has connected to the server!");
				connections.add(connection);
				new OutputThread(connection);
				new InputThread(connection);
				connection.send(new PacketSeed(Tileset.getSeed()));
				for(Entity entity : Entity.getEntities()){
					connection.send(new PacketEntity(entity));
				}
				Player player = new Player(0, 0, false);
				connection.setPlayerUID(player.getUID());
				PacketEntity entity = new PacketEntity(player);
				for(Connection c : connections)
					if(c == connection)
						c.send(new PacketPlayer(player));
					else
						c.send(entity);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private static class InputThread extends Thread{
		
		private Connection connection;
		private ObjectInputStream in;
		
		public InputThread(Connection connection){
			this.connection = connection;
			try{
				this.in = new ObjectInputStream(connection.getSocket().getInputStream());
			} catch(IOException e){
				e.printStackTrace();
			}
			start();
		}
		
		@Override
		public void run(){
			try{
				while(true){
					Packet packet = (Packet)in.readObject();
					handle(connection, packet);
					Sys.sleep(5);
				}
			}catch(Exception e){
				disconnect(connection);
			}
		}
	}
	
	private static class OutputThread extends Thread{
		
		private Connection connection;
		private ObjectOutputStream out;
		
		public OutputThread(Connection connection){
			this.connection = connection;
			try{
				this.out = new ObjectOutputStream(connection.getSocket().getOutputStream());
			} catch(IOException e){
				e.printStackTrace();
			}
			start();
		}
		
		@Override
		public void run(){
			try{
				while(true){
					if(connection.packetAvailable()){
						out.writeObject(connection.poll());
						out.flush();
					}
					Sys.sleep(5);
				}
			}catch(Exception e){
				disconnect(connection);
			}
		}
	}
}