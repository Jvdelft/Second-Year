package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Teen extends Sums{
	public Teen(int x, int y, House h) {
		super(x,y,h);
		max_energy = 75;
		max_faim = 120;
		max_toilet = 100;
		max_happiness = 50;
		energy = max_energy;
		faim = max_faim;
		happiness = max_happiness;
	}
	public Teen() {
		super();
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
