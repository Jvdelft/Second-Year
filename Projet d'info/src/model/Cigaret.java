package model;

public class Cigaret extends DeletableObject{
	public Cigaret(int X, int Y) {
		super(X, Y);
		user = "Teen";
		type = "SMOKE";
		LifePoint = 5;
	}
	public Cigaret() {
		super();
	}
	public void activate (Sums s) {
		System.out.println("Smoke");
		((Teen) s).smoke(this);
		notifyDeletableObserver();
	}
	
	public boolean isObstacle() {
		return false;
	}
	public void makeSprite() {
		sprite = Constantes.cigaret;
	}

}
