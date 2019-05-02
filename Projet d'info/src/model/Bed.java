package model;

public class Bed extends ActivableObject{

	@Override
	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void makeSprite() {
		sprite = Constantes.bedOne;
		
	}

}
