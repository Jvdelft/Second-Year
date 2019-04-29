package model;

public class cigaret extends DeletableObject{
	protected int LifePoint = 3;
	public cigaret(int X, int Y) {
		super(X, Y, 5);
	}
	public cigaret() {
		super();
	}
	public void activate (Sums s) {
		if (s instanceof Teen) {
			((Teen) s).Smoke(this);
			notifyDeletableObserver();
		}
	}
	public boolean isObstacle() {
		return false;
	}
	public void makeSprite() {
		Sprite = Constantes.Door;
	}

}
