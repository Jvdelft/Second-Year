package model;

public class Fridge extends ContainerObject implements Activable{
	public Fridge(int x, int y) {
		super(x,y);
	}
	public Fridge() {
		super();
	}
	public void makeSprite() {
		sprite = Constantes.fridge;
	}

}
