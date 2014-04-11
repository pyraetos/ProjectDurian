package net.pyraetos.durian.entity;

public abstract class ActingEntity extends MovingEntity{

	protected boolean acting;
	
	public ActingEntity(double x, double y, String sprite, boolean focused){
		super(x, y, sprite, focused);
	}

	public boolean isActing(){
		return acting;
	}

	public abstract void act(byte action);
	
}
