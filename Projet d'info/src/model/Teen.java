package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Teen extends Sums{
	public Teen(int x, int y, House h) {
		super(x,y,h);
		max_energy = Constantes.max_energy_teen;
		max_faim = Constantes.max_faim_teen;
		max_toilet = Constantes.max_toilet_teen;
		max_happiness = Constantes.max_happiness_teen;
		if (this.energy == 0) {
			this.faim = max_faim;
			this.energy = max_energy;
			this.happiness = max_happiness;
			this.toilet = 10;
		}
	}
	public Teen() {
		super();
		energy = 1;
	}
	public void smoke(Cigaret c) {
		this.happiness += 1;
		this.age += 1/1000;
	}
	public void makeSprite() {
		try {
    		Sprite_d = ImageIO.read(new File("Images/Sprites Teen M/2.png"));
    		Sprite_l = ImageIO.read(new File("Images/Sprites Teen M/5.png"));
    		Sprite_r = ImageIO.read(new File("Images/Sprites Teen M/8.png"));
    		Sprite_u = ImageIO.read(new File("Images/Sprites Teen M/11.png"));
    		Sprite_face = ImageIO.read(new File("Images/Sprite Teen M Face/1.png"));
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
	}
	

}
