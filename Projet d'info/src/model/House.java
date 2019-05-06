package model;
import java.util.ArrayList;

import view.Window;

public class House extends Building{
	private int money;
	private ArrayList<Sums> habitants = new ArrayList<Sums>();
	private int sizeW = 6;
	private int sizeH = 5;
	private Door door;
	public House(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeW/2)+x,y+sizeH-1, Constantes.mapMaison, 'S');
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
		Window.getInstance().update();
	}

	public boolean isObstacle() {
		return true;
	}
	public int getMoney() {
		return money;
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
		sprite = Constantes.house;
	}
}
