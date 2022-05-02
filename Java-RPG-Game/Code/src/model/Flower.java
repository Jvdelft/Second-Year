package model;

public class Flower extends DeletableObject{
	public Flower() {
		type = "GIVE FLOWER";
		user = "Adult";
		other = true;
		lifePoints = 1;
		price = 10;
	}
	@Override
	public void activate(Sums s) {
		notifyDeletableObserver();
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.flower;
	}

}
