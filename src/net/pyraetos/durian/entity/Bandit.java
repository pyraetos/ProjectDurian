package net.pyraetos.durian.entity;

import net.pyraetos.util.Sys;

@SuppressWarnings("serial")
public class Bandit extends MovingEntity implements Runnable{
	
	public Bandit(double x, double y){
		super(x, y, "bandit/south.png", false);
		Sys.thread(this);
	}
	
	public void run(){
		while(alive){
			if(Sys.chance(.005)){
				double magnitude = Math.floor(4 * Math.random());
				byte direction = (byte)Math.floor(4 * Math.random());
				move(magnitude, direction);
			}
			Sys.sleep(10);
		}
		removeEntity(this);
	}
}
