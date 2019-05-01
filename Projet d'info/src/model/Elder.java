package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Elder extends Sums{
	public Elder(int x, int y, House h) {
		super(x,y,h);
		max_energy = Constantes.max_energy_elder;
		max_faim = Constantes.max_faim_elder;
		max_toilet = Constantes.max_toilet_elder;
		max_happiness = Constantes.max_happiness_elder;
		if (this.energy == 0) {
			this.faim = max_faim;
			this.energy = max_energy;
			this.happiness = max_happiness;
			this.toilet = 10;
		}
	}
	public Elder() {
		super();
		energy = 1;
	}
	public void WatchTV() {
		happiness += 1;
	}
	public void makeSprite() {
		try {
    		Sprite_d = ImageIO.read(new File("Images/Sprites Old Man/0.png"));
    		Sprite_l = ImageIO.read(new File("Images/Sprites Old Man/12.png"));
    		Sprite_r = ImageIO.read(new File("Images/Sprites Old Man/24.png"));
    		Sprite_u = ImageIO.read(new File("Images/Sprites Old Man/36.png"));
    		Sprite_face = ImageIO.read(new File("Images/Sprites Old Man Face/0.png"));
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
		
	}
}
