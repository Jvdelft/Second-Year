package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Kid extends Sums{
	public Kid(int x, int y, House h) {
		super(x, y,h);
		max_energy = 200;
		max_happiness = 200;
		max_toilet = 75;
		max_faim = 80;
		energy = max_energy;
		faim = max_faim;
		happiness = max_happiness;
		type = "PLAY";
		
	}
	public Kid() {
		super();
	}
	public void makeSprite() {
		try {
    		Sprite_d = ImageIO.read(new File("Images/Sprites Kid M/52.png"));
    		Sprite_l = ImageIO.read(new File("Images/Sprites Kid M/64.png"));
    		Sprite_r = ImageIO.read(new File("Images/Sprites Kid M/76.png"));
    		Sprite_u = ImageIO.read(new File("Images/Sprites Kid M/88.png"));
    		Sprite_face = ImageIO.read(new File("Images/Sprites Kid M Face/1.png"));
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
		
	}
	private ArrayList<Toy> toys = new ArrayList<Toy>();
	public void BuyToy() {
		Toy jouet = new Toy(Math.round(this.posX), Math.round(this.posY));
		cost += jouet.getValue();
		toys.add(jouet);
	}
	public void Play(Toy t) {
		
	}
}
