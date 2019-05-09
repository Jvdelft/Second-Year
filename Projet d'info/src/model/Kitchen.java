package model;

import java.util.Random;

import view.InventoryPanel;
import view.MapDrawer;

public class Kitchen extends ContainerObject{
	public Kitchen(int x, int y, Map map) {
		super(x,y);
		type = "START COOKING";
		sizeH = 1;
		sizeW = 3;
		for (int i = 0; i<sizeW-1; i++) {
			Kitchen kitchen = new Kitchen();
			kitchen.setPosX(i+x+1);
			kitchen.setPosY(y);
			map.addObject(kitchen);
		}
	}
	public Kitchen() {
		super();
		type = "START COOKING";
		sprite = null;
	}
	public void cook(Sums s) {
		System.out.println(objectContained);
		double nutritionalValue = 0;
		for (int i= 0; i<this.objectContained.size(); i++) {
			nutritionalValue += ((Food) objectContained.get(i)).getNutritionalValue();
		}
		objectContained.removeAll(objectContained);
		Game.getInstance().playerWait(10000L, s, "Cooking");
		Random rand = new Random();
		double coeff = rand.nextDouble()*2+0.33;
		nutritionalValue*= coeff;
		s.getObjects().add(new Dish((int) Math.round(nutritionalValue)));
		MapDrawer.getInstance().removeDrawContent();
		
		
	}
	public void activate(Sums s) {
		this.open();
		s.setIsPlayable(false);
	}
	public void open() {
		this.type = "COOK";
		MapDrawer.getInstance().drawContent(this);
	}
	public void close() {
		type = "START COOKING";
	}
	public void makeSprite() {
		sprite = Constantes.kitchen;
	}
}
