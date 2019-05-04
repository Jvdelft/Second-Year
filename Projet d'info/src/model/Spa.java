package model;

import view.Window;

public class Spa extends Building{
	private int sizeH = 7;
	private int sizeV = 5;
	private Door door;
	private static Spa SpaInstance;
	private int cleanliness;
	public Spa(int x, int y) {
		super(x,y);
		cleanliness = 10;
		door = new Door(Math.round(sizeH/2)+x-1,y+sizeV-1,Constantes.mapBase, 's');
	}
	public Spa() {
		super();
	}
	public boolean isObstacle() {
		return true;
	}
	public int getSizeH() {
		return sizeH;
	}
	public int getSizeV() {
		return sizeV;
	}
	public Door getDoor() {
		return door;
	}
	public void makeSprite() {
		sprite = Constantes.spa;
	}
	public void activate (Sums s) {
		Game.getInstance().playerWait(5000, s, "activity");
		s.getHouse().changeMoney(-50);
		s.energy += s.max_energy;
		s.happiness = s.max_happiness;
	}
	public static Spa getInstance() {
    	if (SpaInstance == null) {
    		SpaInstance = new Spa(4,10);
    	}
    	return SpaInstance;
    }
}
