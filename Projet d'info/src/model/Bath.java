package model;

public class Bath extends ActivableObject{
	private Map map;
	public Bath(int x, int y, Map map) {
		super(x,y);
		type = "BATH";
		this.map = map;
		sizeH = 2;
		sizeW = 1;
		for (int i = 0; i < sizeH-1 ; i++) {
			Bath bath = new Bath();
			bath.setPosX(x);
			bath.setPosY(y+i+1);
			map.addObject(bath);
		}
	}
	public Bath() {
		super();
		type = "BATH";
		sprite = null;
	}
	public void activate(Sums s) {
		s.hygiene = s.getMaxHygiene();
		s.setHappiness(s.getHappiness()+10);
		Game.getInstance().playerWait(10000, s, null);
	}

	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		sprite = Constantes.bath;
	}
}
