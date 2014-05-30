package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;

@SuppressWarnings("serial")
public class PacketLeave implements Packet{

	private int uid;
	
	public PacketLeave(int uid){
		this.uid = uid;
	}

	@Override
	public void process(){
		Entity.removeEntity(uid);
	}
}
