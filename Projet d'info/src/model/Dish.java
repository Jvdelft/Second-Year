package model;

public class Dish extends Food{
	public Dish(int value) {
		super();
		this.NutritionalValue = value;
	}

	public void makeSprite() {
		sprite = Constantes.dish;
	}
	
}
