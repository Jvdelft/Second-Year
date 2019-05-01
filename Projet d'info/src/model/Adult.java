package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Adult extends Sums {
	public Adult(int x , int y,House h) {
		super(x,y,h);
		max_energy = Constantes.max_energy_adult;
		max_faim = Constantes.max_faim_adult;
		max_toilet = Constantes.max_toilet_adult;
		max_happiness = Constantes.max_happiness_adult;
		type = "GIVE FLOWER";
		ageRange = "Adult";
		if (this.energy == 0) {
			this.faim = max_faim;
			this.energy = max_energy;
			this.happiness = max_happiness;
			this.toilet = 10;
		}
	}
	public Adult() {
		super();
		energy = 1;
	}
	public void work() {
		this.energy -= 70;
		maison.changeMoney(2000); 
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
