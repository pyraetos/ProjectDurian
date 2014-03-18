package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.MovingEntity;

public class PacketMove implements Packet{

	private int uid;
	private byte direction;
	
	public PacketMove(int uid, byte direction){
		this.uid = uid;
		this.direction = direction;
	}
	
	public int getUID(){
		return uid;
	}
	
	public byte getDirection(){
		return direction;
	}
	
	public void process(){
		((MovingEntity)Entity.getEntity(uid)).quickMove(direction);
	}
}
