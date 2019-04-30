package model;

public class Toilet extends ActivableObject {
	public Toilet(int x, int y) {
		super(x,y);
	}
	public boolean isObstacle() {
		return true;
	}
	public void makeSprite() {
		Sprite = Constantes.toilet;
	}
	public void activate (Sums s) {
		s.pee();
	}

}
