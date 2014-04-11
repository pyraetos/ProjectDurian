package net.pyraetos.durian.entity;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.TileConstants;
import net.pyraetos.durian.Tileset;
import net.pyraetos.util.Sounds;

public class Player extends ActingEntity{
	
	public static final byte CUT = 0;
	
	public Player(double x, double y){
		super(x, y, "player/south.png", true);
	}

	@Override
	public void act(byte action){
		if(action == CUT){
			byte type = Tileset.getAdjacentTile(this, direction);
			if(type == Tileset.TREE){
				Tileset.setAdjacentTile(this, direction, Tileset.GRASS);
				Sounds.play("snap.wav");
			}
		}
	}
	
	@Override
	public void move(double magnitude, byte direction){
		super.move(magnitude, direction);
		Durian.setStatus("You are at (" + (int)x + ", " + (int)y + ") which has a tile value of " + TileConstants.describe(x, y) + " in the seed " + Tileset.getSeed());
	}
	
	@Override
	public void teleport(double x, double y){
		double i = Math.floor(x);
		double j = Math.floor(y);
		super.teleport(i, j);
		Durian.setScreen(i - 5, j - 5);
	}
}
