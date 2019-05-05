package model;

public abstract class Building extends GameObject{
	public Building(int x, int y) {
		super(x,y);
	}
	protected Building() { super();}
	public abstract int getSizeH();
	public abstract int getSizeW();
	public abstract boolean isObstacle();
	public abstract Door getDoor();
	public abstract void makeSprite();
	

}
