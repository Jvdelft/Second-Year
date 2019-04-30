package model;
import java.util.ArrayList;

public class House extends Building{
	public int money;
	private ArrayList<Sums> habitants = new ArrayList<Sums>();
	private int sizeW = 5;
	private int sizeH = 4;
	private Door door;
	public House(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeW/2)+x,y+sizeH-1, "MapMaison", 'S');
	}
	public House() {
		super();
	}
	public void AddHabitant(Sums s) {
		habitants.add(s);
	}
	public int TotalMoney() {
		return money;
	}
	public boolean isObstacle() {
		return true;
	}
	public int getSizeW() {
		return sizeW;
	}
	public int getSizeH() {
		return sizeH;
	}
	public Door getDoor() {
		return door;
	}
	public void makeSprite() {
		Sprite = Constantes.house;
	}
}
