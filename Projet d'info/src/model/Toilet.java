package model;

public class Toilet extends ActivableObject {
	public Toilet(int x, int y) {
		super(x,y);
		type = "TOILET";
	}
	public Toilet() {
		super();
		type = "TOILET";
	}
	public boolean isObstacle() {
		return true;
	}
	public void makeSprite() {
		Sprite = Constantes.toilet;
	}
	public void activate (Sums s) {
		Game.getInstance().playerWait(10000L, s, type);
		s.pee();
	}

}
