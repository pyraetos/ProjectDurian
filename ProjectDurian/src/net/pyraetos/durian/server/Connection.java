package net.pyraetos.durian.server;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Connection{

	private Queue<Packet> queue;
	private int playerUID;
	private Socket socket;
	 
	public Connection(Socket socket){
		this.socket = socket;
		this.queue = new LinkedList<Packet>();
	}
	
	public void send(Packet packet){
		queue.add(packet);
	}
	
	public boolean packetAvailable(){
		return !queue.isEmpty();
	}
	
	public Packet poll(){
		return queue.poll();
	}
	
	public int getPlayerUID(){
		return playerUID;
	}
	
	public void setPlayerUID(int playerUID){
		this.playerUID = playerUID;
	}	
	
	public void close(){
		try{
			socket.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Socket getSocket(){
		return socket;
	}
}
