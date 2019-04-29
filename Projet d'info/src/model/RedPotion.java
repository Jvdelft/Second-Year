package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RedPotion extends Drinks{
	public RedPotion(int X, int Y) {
		super(X, Y);
		EnergyValue = 30;
		Toilet = 10;
		LifePoint = 1;
		try {
    		Sprite = ImageIO.read(new File("Projet d'info/Images/RedPotion.png"));
		}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
	}
	public RedPotion() {
		super();
	}

}