package net.pyraetos.durian.entity;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.Tileset;
import net.pyraetos.durian.server.PacketMove;
import net.pyraetos.util.Sys;

public class Player extends MovingEntity{
	
	public Player(double x, double y){
		super(x, y, "guySouth.png");
	}

	public void move(byte direction){
		switch(direction){
		case Sys.NORTH: setSprite("guyNorth.png"); dx = 0; dy = -1; break;
		case Sys.SOUTH: setSprite("guySouth.png"); dx = 0; dy = 1; break;
		case Sys.WEST: setSprite("guyWest.png"); dx = -1; dy = 0; break;
		case Sys.EAST: setSprite("guyEast.png"); dx = 1; dy = 0; break;
		}
		if(!Durian.isOnScreen(x + dx, y + dy))
			Durian.moveScreen(6, direction);
		byte adjacent = Tileset.getAdjacentTile(this, direction);
		if(adjacent == Tileset.TREE)
			return;
		double speed = adjacent == Tileset.WATER ? .25d : 1d;
		Durian.send(new PacketMove(uid, direction, speed));
		move(speed);
	}

	private void move(double speed){
		moving = true;
		for(double i = 0; i < 50; i++){
			if(!moving){
				teleport(x - dx / (i + 1), y - dy / (i + 1));
				return;
			}
			x = Sys.round(x + dx / 50);
			y = Sys.round(y + dy / 50);
			Sys.sleep((int)(1d / speed));
		}
		Durian.setStatus(x + ", " + y + ": " + Tileset.tileGet((int)x, (int)y));
		moving = false;
		dx = dy = 0;
	}
	
	@Override
	public void quickMove(byte direction, double speed){
		switch(direction){
		case Sys.NORTH: setSprite("guyNorth.png"); dx = 0; dy = -1; break;
		case Sys.SOUTH: setSprite("guySouth.png"); dx = 0; dy = 1; break;
		case Sys.WEST: setSprite("guyWest.png"); dx = -1; dy = 0; break;
		case Sys.EAST: setSprite("guyEast.png"); dx = 1; dy = 0; break;
		}
		move(speed);
	}

	@Override
	public void teleport(double x, double y){
		double i = Math.floor(x);
		double j = Math.floor(y);
		super.teleport(i, j);
		Durian.setScreen(i - 5, j - 5);
	}
}
