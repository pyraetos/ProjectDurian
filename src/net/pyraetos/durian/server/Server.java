package net.pyraetos.durian.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.util.Config;
import net.pyraetos.util.Sys;

public class Server extends ServerSocket implements Runnable{

	private static Set<Connection> connections;
	private static Config config;
	private static int nextEntityUID;
	
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
		System.out.println("Battle Server started on port " + port + "!");
		connections = new HashSet<Connection>();
		System.out.println("Generating tileset...");
		//Tileset.generate();
		System.out.println("Server online!");
		Sys.thread(this);
	}

	private void handle(Connection connection, Packet packet){
		packet.process();
		for(Connection bc : connections){
			if(bc != connection){
				bc.send(packet);
			}
		}
	}

	//Disconnect, there's no packet leave
	
	private void disconnect(Connection connection){
		if(!connections.contains(connection)) return;
		connections.remove(connection);
		connection.close();
		int uid = connection.getPlayerUID();
		Entity.removeEntity(uid);
		//for(Connection bc : connections)
		//	bc.send(new PacketLeave(uid));	
		System.out.println(connection.getSocket().getInetAddress() + " has disconnected from the server!");	
	}

	//Joining
	
	private void join(Connection connection){
		//connection.send(new PacketTileset());
		//connection.send(new PacketEntities());
		int uid = nextEntityUID++;
		connection.setPlayerUID(uid);
		//PacketJoinMe pjm = new PacketJoinMe(uid);
		//pjm.process();
		//connection.send(pjm);
		//for(Connection bc : connections)
			//bc.send(new PacketJoin(uid));
	}
	
	@Override
	public void run(){
		while(true){
			try{
				Socket socket = accept();
				System.out.println(socket.getInetAddress() + " has connected to the server!");
				Connection connection = new Connection(socket);
				connections.add(connection);
				new OutputThread(connection);
				new InputThread(connection);
				join(connection);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private class InputThread extends Thread{
		
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
	
	private class OutputThread extends Thread{
		
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