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
	protected String sprite;
	private static int nextEntityUID;
	public static Set<Entity> entities = Sys.concurrentSet(Entity.class);
	
	public Entity(double x, double y, String sprite){
		uid = nextEntityUID++;
		addEntity(this);
		teleport(x, y);
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
	
	protected String fileName(){
		return getClass().getSimpleName();
	}
	
	public static Set<Entity> getEntities(){
		return entities;
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
	
	public static boolean containsEntity(int x, int y){
		for(Entity entity : entities)
			if((int)entity.getX() == x && (int)entity.getY() == y)
				return true;
		return false;
	}
	
	public static void removeEntity(int uid){
		for(Entity entity : entities)
			if(entity.getUID() == uid)
				entities.remove(entity);
	}
	
	public static void removeEntity(Entity entity){
		entities.remove(entity);
	}
	
	public static void addEntity(Entity entity){
		entities.add(entity);
	}

	public static void clearAllEntities(){
		entities.clear();
	}
}
