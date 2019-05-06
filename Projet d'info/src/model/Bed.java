package model;

public class Bed extends Furniture{

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
		Game.getInstance().playerWait(50000, s, s.getAgeRange());
		s.teleportation(this.posX, this.posY);
		s.setDirection(this.direction);
		s.setEnergy(s.getMaxEnergy());
		s.setFaim(20);
		s.setToilet(s.getMaxToilet()-20);
	}

	@Override
	public int getDirection() {
		return 0;
	}

}
