package net.pyraetos.durian.entity;

import java.awt.Image;

import java.io.Serializable;
import java.util.Set;

import net.pyraetos.util.Images;
import net.pyraetos.util.Sys;

public abstract class Entity implements Serializable{

	protected String sprite;
	protected int uid;
	protected double x;
	protected double y;
	public static Set<Entity> entities = Sys.concurrentSet(Entity.class);
	
	public Entity(double x, double y, String sprite){
		teleport(x, y);
		this.sprite = sprite;
		this.uid = -1;
	}
	
	public int getUID(){
		return uid;
	}
	
	public void assign(int uid){
		if(this.uid >= 0 || uid < 0) return;
		this.uid = uid;
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
		if(!(obj instanceof Entity))
			return false;
		Entity other = (Entity)obj;
		if(uid != other.uid)
			return false;
		return true;
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
	
	public static void removeEntity(int uid){
		for(Entity entity : entities)
			if(entity.getUID() == uid)
				entities.remove(entity);
	}
	
	public static void addEntity(Entity entity){
		entities.add(entity);
	}

	public static void clearAllEntities(){
		entities.clear();
	}
}
