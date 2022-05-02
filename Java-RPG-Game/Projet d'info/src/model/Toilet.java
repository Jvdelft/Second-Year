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
	@Override
	public boolean isObstacle() {
		return true;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.toilet;
	}
	@Override
	public void activate (Sums s) {
		Game.getInstance().playerWait(10000L, s, type);
		s.pee();
	}

}
