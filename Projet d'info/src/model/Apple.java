package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Apple extends Food{
	public Apple(int X, int Y) {
		super(X, Y);
		NutritionalValue = 30;
		EnergyValue = 10;
		LifePoints = 1;
	}
	public Apple() {
		super();
	}
	public void makeSprite() {
		sprite = Constantes.apple;
	}


}
