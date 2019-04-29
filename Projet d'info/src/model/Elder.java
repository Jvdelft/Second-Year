package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Elder extends Sums{
	public Elder(int x, int y, House h) {
		super(x,y,h);
		max_energy = 50;
		max_faim = 100;
		max_toilet = 50;
		max_happiness = 100;
		energy = max_energy;
		faim = max_faim;
		happiness = max_happiness;
	}
	public Elder() {
		super();
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
