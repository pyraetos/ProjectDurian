package net.pyraetos.durian.server;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;

@SuppressWarnings("serial")
public class PacketPlayer implements Packet{

	private Player player;
	
	public PacketPlayer(Player player){
		this.player = player;
	}

	@Override
	public void process(){
		Durian.setPlayer(player);
		Entity.addEntity(player);
	}
}
