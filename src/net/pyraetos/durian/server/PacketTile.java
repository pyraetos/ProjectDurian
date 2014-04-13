package net.pyraetos.durian.server;

import net.pyraetos.durian.Tileset;

public class PacketTile implements Packet{

	private int x;
	private int y;
	private double type;
	
	public PacketTile(int x, int y, double type){
		this.x = x;
		this.y = y;
		this.type = type;
	}

	@Override
	public void process(){
		Tileset.tileSet(x, y, type);
	}
}
