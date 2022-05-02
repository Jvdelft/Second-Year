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
		type = "START COOKING";		//La cuisine prend trois cases donc le sprite est dessin� une fois et les deux autres cases ont besoin d'un constructeur mettant leur sprite � null
		sprite = null;
	}
	public void cook(Sums s) {
		double nutritionalValue = 0;
		for (int i= 0; i<this.objectContained.size(); i++) {
			nutritionalValue += ((Food) objectContained.get(i)).getNutritionalValue();	//Le personnage cuisine et il obtient un plat avec une valeur nutrionelle al�atoire 
		}
		objectContained.removeAll(objectContained);
		Game.getInstance().playerWait(10000L, s, "Cooking");
		Random rand = new Random();
		double coeff = rand.nextDouble()*2+0.33;
		nutritionalValue*= coeff;
		s.getObjects().add(new Dish((int) Math.round(nutritionalValue)));
		MapDrawer.getInstance().removeDrawContent();
		
		
	}
	@Override
	public void activate(Sums s) {
		this.open();
		s.setIsPlayable(false);
	}
	@Override
	public void open() {
		this.type = "COOK";
		MapDrawer.getInstance().drawContent(this);	//Une fois le container ouvert, le personnage a commenc� � cuisiner, le type est donc cook.
	}
	@Override
	public void close() {
		type = "START COOKING";
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.kitchen;
	}
}
