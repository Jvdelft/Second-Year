package model;

public class Bed extends Furniture{
	private Map map;
	public Bed(int x, int y, Map map) {
		super(x,y);
		type = "SLEEP";
		this.map = map;
		sizeH = 2;
		sizeW = 1;
		for (int i = 0; i < sizeH-1 ; i++) {
			Bed bed = new Bed();
			bed.setPosX(x);
			bed.setPosY(y+i+1);
			map.addObject(bed);
		}
	}
	public Bed() {
		super();
		type = "SLEEP";
		sprite = null;
		sizeH = 2;
		sizeW = 1;
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
		return direction;
	}
	
	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		sprite = Constantes.bedOne;
		
	}

}
