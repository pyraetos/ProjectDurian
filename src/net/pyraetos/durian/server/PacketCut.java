package net.pyraetos.durian.server;

import net.pyraetos.durian.ProjectDurian;
import net.pyraetos.durian.Tileset;
import net.pyraetos.util.Sounds;

public class PacketCut implements Packet{

	private int x;
	private int y;
	private byte direction;
	
	public PacketCut(int x, int y, byte direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	@Override
	public void process(){
		Tileset.setAdjacentTile(x, y, direction, Tileset.GRASS);
		if(!ProjectDurian.isServer())
			Sounds.play("snap.wav");
	}
}
