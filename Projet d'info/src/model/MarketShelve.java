package model;

public class MarketShelve extends ContainerObject{
	public MarketShelve(int x, int y) {
		super(x,y);
		sizeH = 1;
		sizeV = 2;
	}
	public MarketShelve() {
		super();
	}
	public void makeSprite() {
		sprite = Constantes.étagère;
	}
}
