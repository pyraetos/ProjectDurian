package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.MovingEntity;

public class PacketMove implements Packet{

	private int uid;
	private byte direction;
	private double speed;
	
	public PacketMove(int uid, byte direction, double speed){
		this.uid = uid;
		this.direction = direction;
		this.speed = speed;
	}
	
	public int getUID(){
		return uid;
	}
	
	public byte getDirection(){
		return direction;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void process(){
		((MovingEntity)Entity.getEntity(uid)).quickMove(direction, speed);
	}
}
