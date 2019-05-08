package model;

public class Sofa extends Furniture implements Directable{
	private Map map;
	public Sofa(int x, int y, Map map) {
		super(x,y);
		type = "SIT";
		this.map = map;
		sizeH = 2;
		sizeW = 1;
		for (int i = 0; i < sizeH-1 ; i++) {
			Sofa sofa = new Sofa();
			sofa.setPosX(x);
			sofa.setPosY(y+i+1);
			map.addObject(sofa);
		}
	}
		public Sofa() {
			super();
			type = "SIT";
			sprite = null;
			sizeH = 2;
			sizeW = 1;
		}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		switch (direction) {
		case NORTH : sprite = Constantes.sofaNorth; break;
		case EAST : sprite = Constantes.sofaEast; break;
		case WEST : sprite = Constantes.sofaWest; break;
		case SOUTH : sprite = Constantes.sofaSouth; break;
		}
	}

	@Override
	public int getDirection() {
		return direction;
	}
	public void activate(Sums s) {
		Game.getInstance().playerWait(5000, s, s.getAgeRange());
		s.teleportation(this.posX, this.posY);
		s.setDirection(this.direction);
		s.setEnergy((int) (s.getEnergy() + 10));
	}

}
