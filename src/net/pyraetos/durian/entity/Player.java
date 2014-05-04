package net.pyraetos.durian.entity;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.Status;
import net.pyraetos.durian.Tileset;
import net.pyraetos.durian.server.PacketCut;
import net.pyraetos.durian.server.PacketKill;
import net.pyraetos.util.Sounds;

public class Player extends MovingEntity{
	
	public Player(double x, double y, boolean focused){
		super(x, y, "player/south.png", focused);
	}

	public void cut(){
		byte type = Tileset.getAdjacentTile(this, direction);
		if(type == Tileset.TREE){
			if(Durian.isOnline())
				Durian.send(new PacketCut((int)x, (int)y, direction));
			Tileset.setAdjacentTile(this, direction, Tileset.GRASS);
			Sounds.play("snap.wav");
		}
		Entity entity = getEntity(this, direction);
		if(entity != null && entity instanceof Bandit){
			entity.setAlive(false);
			if(Durian.isOnline()){
				Durian.send(new PacketKill(entity.getUID()));
				removeEntity(entity);
			}
			Sounds.play("snap.wav");
		}
	}

	@Override
	public void move(double magnitude, byte direction){
		super.move(magnitude, direction);
		Status.set("You are at (" + (int)x + ", " + (int)y + ") which has a tile value of " + Tileset.describe(x, y) + " in the seed " + Tileset.getSeed());
	}
	
	@Override
	public void teleport(double x, double y){
		double i = Math.floor(x);
		double j = Math.floor(y);
		super.teleport(i, j);
		Durian.setScreen(i - 5, j - 5);
	}
}
