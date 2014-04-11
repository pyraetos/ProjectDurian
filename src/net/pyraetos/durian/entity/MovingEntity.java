package net.pyraetos.durian.entity;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.Tileset;
import net.pyraetos.util.Sys;

public abstract class MovingEntity extends Entity {

	protected double speed = 1.0d;
	protected byte direction = Sys.SOUTH;
	protected boolean moving;
	protected boolean focused;
	
	public MovingEntity(double x, double y, String sprite, boolean focused){
		super(x, y, sprite);
		this.focused = focused;
	}
	
	public void move(double magnitude, byte direction){
		this.direction = direction;
		double dx, dy;
		switch(direction){
		case Sys.NORTH: dx = 0; dy = -magnitude; setSprite(fileName() + "/north.png"); break;
		case Sys.SOUTH: dx = 0; dy = magnitude; setSprite(fileName() + "/south.png"); break;
		case Sys.WEST:  dx = -magnitude; dy = 0; setSprite(fileName() + "/west.png"); break;
		case Sys.EAST: dx = magnitude; dy = 0; setSprite(fileName() + "/east.png"); break;
		default: return;
		}
		if(focused && !Durian.isOnScreen(x + dx, y + dy))
			Durian.moveScreen(6, direction);
		byte adjacent = Tileset.getAdjacentTile(this, direction);
		if(adjacent == Tileset.TREE)
			return;
		moving = true;
		speed = adjacent == Tileset.WATER ? .25d : 1d;
		for(double i = 0; i < 50 * magnitude; i++){
			if(!moving) break;
			x = Sys.round(x + dx / (50 * magnitude));
			y = Sys.round(y + dy / (50 * magnitude));
			Sys.sleep((int)(1d / speed));
		}
		moving = false;
		speed = 1d;
	}
	
	public void setFocused(boolean focused){
		this.focused = focused;
	}
	
	public boolean isFocused(){
		return focused;
	}
	
	public boolean isMoving(){
		return moving;
	}

	public byte getDirection(){
		return direction;
	}
	
	public double getSpeed(){
		return speed;
	}
}