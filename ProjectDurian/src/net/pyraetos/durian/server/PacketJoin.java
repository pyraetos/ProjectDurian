package net.pyraetos.durian.server;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;

@SuppressWarnings("serial")
public class PacketJoin implements Packet{

	private Player player;
	
	public PacketJoin(Player player){
		this.player = player;
	}
	
	@Override
	public void process(){
		Entity.addEntity(player);
	}
}
