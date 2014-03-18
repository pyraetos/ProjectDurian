package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;

public class PacketJoinMe implements Packet{
	
	private int uid;
	
	public PacketJoinMe(int uid){
		this.uid = uid;
	}
	
	public int getUID(){
		return uid;
	}
	
	public void process(){
		Player player = new Player(5, 5);
		player.assign(uid);
		Entity.addEntity(player);
	}
}
