package model;

public class Cigaret extends DeletableObject{
	public Cigaret(int X, int Y) {
		super(X, Y);
		setValues();
	}
	public Cigaret() {
		super();
		setValues();
		
	}
	@Override
	public void activate (Sums s) {
		if (s instanceof Teen) {
			((Teen) s).smoke(this);
			notifyDeletableObserver();
		}
	}
	private void setValues() {
		user = "Teen";
		type = "SMOKE";
		lifePoints = 5;
		price = 10;
	}
	@Override
	public boolean isObstacle() {
		return false;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.cigaret;
	}

}
