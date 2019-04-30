package model;

public class Market extends Building{
	private int sizeH = 5;
	private int sizeV = 4;
	private Door door;
	public Market(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeH/2)+x,y+sizeV-1,"MapMarket", 'S');
	}
	public Market() {
		super();
	}
	public boolean isObstacle() {
		return true;
	}
	public int getSizeH() {
		return sizeH;
	}
	public int getSizeV() {
		return sizeV;
	}
	public Door getDoor() {
		return door;
	}
	public void makeSprite() {
		Sprite = Constantes.market;
	}
}

