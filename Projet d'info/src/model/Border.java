package model;

public class Border extends GameObject{
	public Border(int x, int y) {
		super(x,y);
	}
	public Border() {
		super();
	}
	public void makeSprite() {
	}
	public boolean isObstacle() {
		return true;
	}

}
