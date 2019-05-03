package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import view.Window;

public class Adult extends Sums {
	public Adult(int x , int y,House h) {
		super(x,y,h);
		max_energy = Constantes.max_energy_adult;
		max_faim = Constantes.max_faim_adult;
		max_toilet = Constantes.max_toilet_adult;
		max_happiness = Constantes.max_happiness_adult;
		type = "GIVE FLOWER";
		typeAffection = "MAKE LOVE";
		ageRange = "Adult";
		user = "Adult";
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
	public void receiveFlower(Sums s) {
		s.getHouse().changeMoney(-10);
		this.interraction(s, 10);
		Window.getInstance().update();
    }
	public void makeLove() {
		Game.getInstance().makeBaby(this.getHouse());
	}

	public void makeSprite() {
    	sprite_d = Constantes.spriteDAdult;
    	sprite_l = Constantes.spriteLAdult;
    	sprite_r = Constantes.spriteRAdult;
    	sprite_u = Constantes.spriteUAdult;
    	sprite_face = Constantes.spriteFaceAdult;
	}
}
