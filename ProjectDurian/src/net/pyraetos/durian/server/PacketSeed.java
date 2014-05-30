package net.pyraetos.durian.server;

import net.pyraetos.durian.Tileset;

@SuppressWarnings("serial")
public class PacketSeed implements Packet{

	private long seed;
	
	public PacketSeed(long seed){
		this.seed = seed;
	}

	@Override
	public void process(){
		Tileset.setSeed(seed);
	}
}
