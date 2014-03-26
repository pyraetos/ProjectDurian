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
		Durian.send(new PacketMove(uid, direction));
		move();
	}

	private void move(){
		moving = true;
		for(double i = 0; i < 50; i++){
			if(!moving){
				teleport(x - dx / (i + 1), y - dy / (i + 1));
				return;
			}
			x = Sys.round(x + dx / 50);
			y = Sys.round(y + dy / 50);
			Sys.sleep(1);
		}
		Durian.setStatus("Point: " + x + ", " + y + " Value: " + Tileset.tileGet((int)x, (int)y) + " Type: " + Tileset.getTile((int)x, (int)y));
		moving = false;
		dx = dy = 0;
	}
	
	@Override
	public void quickMove(byte direction){
		switch(direction){
		case Sys.NORTH: setSprite("guyNorth.png"); dx = 0; dy = -1; break;
		case Sys.SOUTH: setSprite("guySouth.png"); dx = 0; dy = 1; break;
		case Sys.WEST: setSprite("guyWest.png"); dx = -1; dy = 0; break;
		case Sys.EAST: setSprite("guyEast.png"); dx = 1; dy = 0; break;
		}
		move();
	}
}
