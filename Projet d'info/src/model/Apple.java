package model;

import java.io.File; 
import java.io.IOException;

import javax.imageio.ImageIO;

public class Apple extends Food{
	public Apple(int X, int Y) {
		super(X, Y);
		setValues();
	}
	public Apple() {
		super();
		setValues();
	}
	private void setValues() {
		NutritionalValue = 30;
		EnergyValue = 10;
		lifePoints = 1;
		price = 1;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.apple;
	}


}
