package model;
import java.util.ArrayList;

public class House extends Building{
	private int money;
	private ArrayList<Sums> habitants = new ArrayList<Sums>();
	private int sizeH = 6;
	private int sizeV = 5;
	private Door door;
	public House(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeH/2)+x,y+sizeV-1, Constantes.mapMaison, 'S');
		money = 100;
	}
	public House() {
		super();
	}
	public void AddHabitant(Sums s) {
		habitants.add(s);
	}
	public void changeMoney(int i) {
		money += i;
	}

	public boolean isObstacle() {
		return true;
	}
	public int getMoney() {
		return money;
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
		sprite = Constantes.house;
	}
}
