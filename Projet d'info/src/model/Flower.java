package model;

public class Flower extends DeletableObject{
	public Flower() {
		type = "GIVE FLOWER";
		user = "Adult";
		other = true;
		lifePoints = 1;
		price = 10;
	}
	public void activate(Sums s) {
		notifyDeletableObserver();
	}
	public void makeSprite() {
		sprite = Constantes.flower;
	}

}
