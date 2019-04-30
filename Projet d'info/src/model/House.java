package model;
import java.util.ArrayList;

public class House extends Building{
	private int money = 100;
	private ArrayList<Sums> habitants = new ArrayList<Sums>();
	private int sizeH = 5;
	private int sizeV = 4;
	private Door door;
	public House(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeH/2)+x,y+sizeV-1, "MapMaison", 'S');
	}
	public House() {
		super();
	}
	public void AddHabitant(Sums s) {
		habitants.add(s);
	}
	public void changeMoney(int i) {
		money += i;
		System.out.println(money);
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
		Sprite = Constantes.house;
	}
}
