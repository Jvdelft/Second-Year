package model;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import view.Window;

public class House extends Building implements Serializable{
	private int money;
	private ArrayList<Sums> habitants = new ArrayList<Sums>();
	private int sizeW = 6;
	private int sizeH = 5;
	private Door door;
	protected transient BufferedImage spriteHouse1;
	protected transient BufferedImage spriteHouse2;
	private int category = 1;
	public House(int x, int y) {
		super(x,y);
		door = new Door(Math.round(sizeW/2)+x,y+sizeH-1, Constantes.mapMaison, 'S');	//Les maisons possèdent une porte et l'argent de la famille.
		money = 100;
	}
	public void AddHabitant(Sums s) {
		habitants.add(s);
	}
	public void changeMoney(int i) {
		money += i;
		Window.getInstance().update();
	}
	public void setCategory(int i) {		//Changement de maison lors de l'achat d'une nouvelle.
		category = i;
		String oldHouse = Constantes.mapMaison;
		String newHouse = Constantes.mapMaison2;
		if (category == 2) {
			sizeW=6;
			sizeH=5;
		}
		Door d = new Door(Math.round(sizeW/2)+this.getPosX(),this.getPosY()+sizeH-1, Constantes.mapMaison2, 'S');
		this.setPosX(this.getPosX()+1);
		Game.getInstance().getMaps().get(Constantes.mapBase).newDoor(this, d);
		ArrayList<Sums> sums = Game.getInstance().getMaps().get(oldHouse).getSumsOnMap();
		Game.getInstance().getMaps().get(newHouse).addSumsOnMap(sums);
		Game.getInstance().getMaps().get(newHouse).setIsInitHouse(true);
		door = d;
	}
	public int getMoney() {
		return money;
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
	public BufferedImage getSprite() {
		BufferedImage sprite = null;
		switch (category) {
		case 1 : sprite = spriteHouse1; break;
		case 2 : sprite = spriteHouse2; break;

		}
		return sprite;
	}
	@Override
	public void makeSprite() {
		spriteHouse1 = Constantes.house;
		spriteHouse2 = Constantes.house2;
	}
}
