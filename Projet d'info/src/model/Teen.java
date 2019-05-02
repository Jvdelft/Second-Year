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
		sprite_d = Constantes.spriteDTeen;
    	sprite_l = Constantes.spriteLTeen;
    	sprite_r = Constantes.spriteRTeen;
    	sprite_u = Constantes.spriteUTeen;
    	sprite_face = Constantes.spriteFaceTeen;
	}
	

}
