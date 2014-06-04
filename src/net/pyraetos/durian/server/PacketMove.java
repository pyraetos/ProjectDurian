package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.MovingEntity;

public class PacketMove implements Packet{

	private int uid;
	private double magnitude;
	private byte direction;
	
	public PacketMove(int uid, double magnitude, byte direction){
		this.uid = uid;
		this.magnitude = magnitude;
		this.direction = direction;
	}

	@Override
	public void process(){
		MovingEntity movingEntity = (MovingEntity)Entity.getEntity(uid);
		movingEntity.remoteMove(magnitude, direction);
	}
}
