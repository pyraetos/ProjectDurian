package net.pyraetos.durian.server;

import net.pyraetos.durian.ProjectDurian;
import net.pyraetos.durian.entity.Entity;
import net.pyraetos.util.Sounds;

public class PacketKill implements Packet{

	private int uid;
	
	public PacketKill(int uid){
		this.uid = uid;
	}

	@Override
	public void process(){
		Entity.getEntity(uid).setAlive(false);
		if(!ProjectDurian.isServer()){
			Sounds.play("snap.wav");
			Entity.removeEntity(uid);
		}
	}
}
