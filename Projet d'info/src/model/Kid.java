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
		ageRange = "Kid";
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
		sprite_d = Constantes.spriteDKid;
    	sprite_l = Constantes.spriteLKid;
    	sprite_r = Constantes.spriteRKid;
    	sprite_u = Constantes.spriteUKid;
    	sprite_face = Constantes.spriteFaceKid;
		
	}
	private ArrayList<Toy> toys = new ArrayList<Toy>();
	public void BuyToy() {
		Toy jouet = new Toy(Math.round(this.posX), Math.round(this.posY));
		cost += jouet.getValue();
		toys.add(jouet);
	}
	public void play(Toy t) {
		this.happiness += t.BeHappy();
		
	}
}
