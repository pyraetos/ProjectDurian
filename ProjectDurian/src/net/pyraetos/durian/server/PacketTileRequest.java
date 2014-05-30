package net.pyraetos.durian.server;

import net.pyraetos.durian.Tileset;

@SuppressWarnings("serial")
public class PacketTileRequest implements Packet{

	private int x;
	private int y;

	public PacketTileRequest(int x, int y){
		this.x = x;
		this.y = y;
	}

	public PacketTile getPacketTile(){
		double type = Tileset.tileGet(x, y);
		if(type == Tileset.NULL){
			Tileset.pgenerate(x, y);
			type = Tileset.tileGet(x, y);
		}
		return new PacketTile(x, y, type);
	}
	
	@Override
	public void process(){
		
	}
}
