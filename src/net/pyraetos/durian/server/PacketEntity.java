package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;

@SuppressWarnings("serial")
public class PacketEntity implements Packet{

	private Entity entity;
	
	public PacketEntity(Entity entity){
		this.entity = entity;
	}

	@Override
	public void process(){
		Entity.addEntity(entity);
	}
}
