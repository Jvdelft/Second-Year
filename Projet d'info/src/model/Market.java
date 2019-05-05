package model;

public class Market extends Building{
	private int sizeW = 5;
	private int sizeH = 4;
	private Door door;
	public Market(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeH/2)+x,y+sizeH-1,Constantes.mapMarket, 'S');
	}
	public Market() {
		super();
	}
	public boolean isObstacle() {
		return true;
	}
	public int getSizeW() {
		return sizeW;
	}
	public int getSizeH() {
		return sizeH;
	}
	public Door getDoor() {
		return door;
	}
	public void makeSprite() {
		sprite = Constantes.market;
	}
}

