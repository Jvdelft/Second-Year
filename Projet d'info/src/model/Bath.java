package model;

public class Bath extends ActivableObject{
	public Bath(int x, int y) {
		super(x,y);
	}
	public void activate(Sums s) {
		
	}

	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		// TODO Auto-generated method stub
		
	}
}
