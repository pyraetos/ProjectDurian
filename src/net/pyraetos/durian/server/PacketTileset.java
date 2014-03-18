package net.pyraetos.durian.server;

import java.util.LinkedList;

import net.pyraetos.durian.TileRegion;
import net.pyraetos.durian.Tileset;

public class PacketTileset implements Packet{

	private LinkedList<LinkedList<TileRegion>> tr;
	
	public PacketTileset(){
		this.tr = Tileset.getAllRegions();
	}
	
	public void process(){
		Tileset.setTileset(tr);
	}
}
