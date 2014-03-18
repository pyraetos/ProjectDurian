package net.pyraetos.durian.entity;

import net.pyraetos.util.Sys;

public class Friend extends Player{

	public Friend(Player player){
		super(player.getX(), player.getY());
		uid = player.getUID();
	}
	
	@Override
	public void move(byte direction){
		switch(direction){
		case Sys.NORTH: setSprite("guyNorth.png"); dx = 0; dy = -1; break;
		case Sys.SOUTH: setSprite("guySouth.png"); dx = 0; dy = 1; break;
		case Sys.WEST: setSprite("guyWest.png"); dx = -1; dy = 0; break;
		case Sys.EAST: setSprite("guyEast.png"); dx = 1; dy = 0; break;
		}
		moving = true;
		for(int i = 0; i < 50; i++){
			x += dx;
			y += dy;
			Sys.sleep(5);
		}
		moving = false;
	}
}
