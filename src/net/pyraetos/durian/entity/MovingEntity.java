package net.pyraetos.durian.entity;

public abstract class MovingEntity extends Entity {

	protected double dx;
	protected double dy;
	protected byte direction;
	protected boolean moving;
	
	public MovingEntity(double x, double y, String sprite) {
		super(x, y, sprite);
		this.moving = false;
	}

	public abstract void quickMove(byte direction);
	
	public abstract void move(byte direction);
	
	public boolean isMoving(){
		return moving;
	}
	
	public double getDeltaX(){
		return dx;
	}
	
	public double getDeltaY(){
		return dy;
	}
	
	public void interruptMovement(){
		moving = false;
		dx = dy = 0;
	}

}