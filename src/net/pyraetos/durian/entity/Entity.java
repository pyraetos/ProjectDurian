package net.pyraetos.durian.entity;

import java.awt.Image;
import java.io.Serializable;
import java.util.Set;

import net.pyraetos.util.Images;
import net.pyraetos.util.Sys;

public abstract class Entity implements Serializable{
	
	protected int uid;
	protected double x;
	protected double y;
	protected boolean alive;
	protected String sprite;
	private static int nextEntityUID;
	public static Set<Entity> entities = Sys.concurrentSet(Entity.class);
	
	public Entity(double x, double y, String sprite){
		uid = nextEntityUID++;
		alive = true;
		addEntity(this);
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public int getUID(){
		return uid;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public Image getSprite(){
		return Images.retrieve(sprite);
	}

	public void setSprite(String dir){
		sprite = dir;
	}

	public void teleport(double x, double y){
		this.x = x;
		this.y = y;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Entity other = (Entity)obj;
		if(uid != other.uid)
			return false;
		return true;
	}

	protected String fileName(){
		return getClass().getSimpleName();
	}
	
	public static void setEntities(Set<Entity> entities){
		Entity.entities = entities;
	}
	
	public static Set<Entity> getEntities(){
		return entities;
	}
	
	public static void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public static Entity getEntity(int uid){
		for(Entity entity : entities)
			if(entity.getUID() == uid)
				return entity;
		return null;
	}
	
	public static Entity getEntity(Entity entity, byte direction){
		int x = (int)entity.getX();
		int y = (int)entity.getY();
		switch(direction){
		case Sys.NORTH: y -= 1; break;
		case Sys.SOUTH: y += 1; break;
		case Sys.WEST: x -= 1; break;
		case Sys.EAST: x += 1; break;
		default: return null;
		}
		for(Entity e : entities)
			if((int)e.getX() == x && (int)e.getY() == y)
				return e;
		return null;
	}
	
	public static Entity getEntity(int x, int y){
		for(Entity e : entities)
			if((int)e.getX() == x && (int)e.getY() == y)
				return e;
		return null;
	}
	
	public static boolean containsEntity(int x, int y){
		return getEntity(x, y) == null ? false : true;
	}
	
	public static void removeEntity(int uid){
		for(Entity entity : entities)
			if(entity.getUID() == uid)
				entities.remove(entity);
	}
	
	public static void removeEntity(Entity entity){
		entities.remove(entity);
	}

	public static void clearAllEntities(){
		entities.clear();
	}
}
