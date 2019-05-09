package model;

public class Market extends Building{
	private int sizeW = 5;
	private int sizeH = 4;
	private Door door;
	public Market(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeW/2)+x,y+sizeH-1,Constantes.mapMarket, 'S');	//Le market possède une porte.
	}
	public Market() {
		super();
	}
	@Override
	public boolean isObstacle() {
		return true;
	}
	@Override
	public int getSizeW() {
		return sizeW;
	}
	@Override
	public int getSizeH() {
		return sizeH;
	}
	@Override
	public Door getDoor() {
		return door;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.market;
	}
}

