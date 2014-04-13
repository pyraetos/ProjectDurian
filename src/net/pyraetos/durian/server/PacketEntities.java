package net.pyraetos.durian.server;

import java.util.Set;

import net.pyraetos.durian.entity.Entity;

public class PacketEntities implements Packet{

	private Set<Entity> entities;
	
	public PacketEntities(Set<Entity> entities){
		this.entities = entities;
	}

	@Override
	public void process(){
		Entity.setEntities(entities);
	}
}
