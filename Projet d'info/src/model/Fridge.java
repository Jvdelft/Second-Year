package model;

public class Fridge extends ContainerObject implements Activable{
	public Fridge(int x, int y) {
		super(x,y);
		for (int i = 0; i<6; i++) {
			if (i== 3) {
				objectContained.add(new Cigaret());
				continue;
			}
			objectContained.add(new Apple());
		}
	}
	public Fridge() {
		super();
	}
	public void makeSprite() {
		sprite = Constantes.fridge;
	}

}
