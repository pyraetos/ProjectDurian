package net.pyraetos.durian.entity;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.ProjectDurian;
import net.pyraetos.durian.Tileset;
import net.pyraetos.durian.server.PacketMove;
import net.pyraetos.durian.server.Server;
import net.pyraetos.util.Sys;

@SuppressWarnings("serial")
public abstract class MovingEntity extends Entity {

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
		moving = true;
		for(double a = 0; a < magnitude; a++){
			int p = (int)(x + dx / magnitude);
			int q = (int)(y + dy / magnitude);
			byte type = Tileset.getTile(p, q);
			if(type == Tileset.NULL || type == Tileset.TREE || containsEntity(p, q)) break;
			if(ProjectDurian.isServer())
				Server.sendAll(new PacketMove(uid, 1, direction));
			if(Durian.isOnline())
				Durian.send(new PacketMove(uid, 1, direction));
			double speed = type == Tileset.WATER ? .25d : .5d;
			for(int b = 0; b < 50; b++){
				if(!moving) break;
				x = Sys.round(x + dx / (50 * magnitude));
				y = Sys.round(y + dy / (50 * magnitude));
				Sys.sleep((long)(1d / speed));
			}
		}
		moving = false;
	}
	
	public void remoteMove(double magnitude, byte direction){
		this.direction = direction;
		double dx, dy;
		switch(direction){
		case Sys.NORTH: dx = 0; dy = -magnitude; setSprite(fileName() + "/north.png"); break;
		case Sys.SOUTH: dx = 0; dy = magnitude; setSprite(fileName() + "/south.png"); break;
		case Sys.WEST:  dx = -magnitude; dy = 0; setSprite(fileName() + "/west.png"); break;
		case Sys.EAST: dx = magnitude; dy = 0; setSprite(fileName() + "/east.png"); break;
		default: return;
		}
		moving = true;
		for(double a = 0; a < magnitude; a++){
			int p = (int)(x + dx / magnitude);
			int q = (int)(y + dy / magnitude);
			byte type = Tileset.getTile(p, q);
			double speed = type == Tileset.WATER ? .25d : .5d;
			for(int b = 0; b < 50; b++){
				if(!moving) break;
				x = Sys.round(x + dx / (50 * magnitude));
				y = Sys.round(y + dy / (50 * magnitude));
				Sys.sleep((long)(1d / speed));
			}
		}
		moving = false;
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

}