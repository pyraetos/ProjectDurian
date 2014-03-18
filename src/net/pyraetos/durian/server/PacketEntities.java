package net.pyraetos.durian.server;

import java.util.Set;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.MovingEntity;

public class PacketEntities implements Packet{

	private Set<Entity> entities;
	
	public PacketEntities(){
		entities = Entity.getEntities();
	}

	public void process(){
		for(Entity entity : entities){
			if(entity instanceof MovingEntity){
				MovingEntity me = (MovingEntity)entity;
				me.interruptMovement();
			}
			Entity.addEntity(entity);
		}
	}
}
