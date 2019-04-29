package model;

public class Cigaret extends DeletableObject{
	protected int LifePoint = 3;
	public Cigaret(int X, int Y) {
		super(X, Y, 5);
	}
	public Cigaret() {
		super();
	}
	public void activate (Sums s) {
		if (s instanceof Teen) {
			((Teen) s).smoke(this);
			notifyDeletableObserver();
		}
	}
	public boolean isObstacle() {
		return false;
	}
	public void makeSprite() {
		Sprite = Constantes.door;
	}

}
