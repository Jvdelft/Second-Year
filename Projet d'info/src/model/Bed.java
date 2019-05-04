package model;

public class Bed extends ActivableObject{

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		sprite = Constantes.bedOne;
		
	}

	@Override
	public void activate(Sums s) {
		// TODO Auto-generated method stub
		
	}

}
