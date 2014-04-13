package net.pyraetos.durian.server;

import net.pyraetos.durian.Durian;
import net.pyraetos.durian.entity.Entity;
import net.pyraetos.durian.entity.Player;

public class PacketJoin implements Packet{

	private Player player;
	private boolean me;
	
	public PacketJoin(Player player){
		this.player = player;
	}

	public void setMe(boolean me){
		this.me = me;
	}
	
	@Override
	public void process(){
		if(me)
			Durian.setPlayer(player);
		Entity.addEntity(player);
	}
}
