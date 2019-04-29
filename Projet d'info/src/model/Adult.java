package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Adult extends Sums {
	public Adult(int x , int y,House h) {
		super(x,y,h);
		max_energy = 100;
		max_faim = 100;
		max_toilet = 100;
		max_happiness = 100;
		energy = max_energy;
		faim = max_faim;
		happiness = max_happiness;
	}
	public Adult() {
		super();
	}
	public void work() {
		this.energy -= 70;
		maison.money += 2000; 
	}
	public void makeSprite() {
		try {
    		Sprite_d = ImageIO.read(new File("Images/Sprites Man/image_part_056.png"));
    		Sprite_l = ImageIO.read(new File("Images/Sprites Man/image_part_068.png"));
    		Sprite_r = ImageIO.read(new File("Images/Sprites Man/image_part_080.png"));
    		Sprite_u = ImageIO.read(new File("Images/Sprites Man/image_part_092.png"));
    		Sprite_face = ImageIO.read(new File("Images/Sprite Man Face/0.png"));
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
	}
}
