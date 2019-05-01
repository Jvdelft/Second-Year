package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Kid extends Sums{
	public Kid(int x, int y, House h) {
		super(x, y,h);
		max_energy = Constantes.max_energy_kid;
		max_happiness = Constantes.max_happiness_kid;
		max_toilet = Constantes.max_toilet_kid;
		max_faim = Constantes.max_faim_kid;
		type = "PLAY";
		if (this.energy == 0) {
			this.faim = max_faim;
			this.energy = max_energy;
			this.happiness = max_happiness;
			this.toilet = 10;
		}
		
	}
	public Kid() {
		super();
		energy = 1;
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
