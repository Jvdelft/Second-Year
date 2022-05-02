package model;

import view.Window;

public class Spa extends Building{
	private int sizeW = 7;
	private int sizeH = 5;
	private Door door;
	private static Spa SpaInstance;
	private int cleanliness;
	private Spa(int x, int y) {
		super(x,y);
		cleanliness = 10;
		door = new Door(sizeW/2+x-1,y+sizeH-1,Constantes.mapBase, 's');
	}
	public Spa() {
		super();
	}
	public void activate (Sums s) {
		Game.getInstance().playerWait(5000, s, "activity");
		s.getHouse().changeMoney(-50);
		s.setEnergy(s.max_energy);
		s.setHappiness(s.getMaxHappiness());
		s.setHygiene(s.getHygiene());
	}
	public static Spa getInstance() {
    	if (SpaInstance == null) {
    		SpaInstance = new Spa(4,10);
    	}
    	return SpaInstance;
    }
	@Override
	public boolean isObstacle() {
		return true;
	}
	@Override
	public int getSizeW() {
		return sizeW;
	}
	@Override
	public int getSizeH() {
		return sizeH;
	}
	@Override
	public Door getDoor() {
		return door;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.spa;
	}
}
