package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Kid extends Sums{
	public Kid(int x, int y, House h) {
		super(x, y,h);
		max_energy = Constantes.max_energy_kid;
		max_happiness = Constantes.max_happiness_kid;	//on initialise l'enfant avec ses besoins au maximum.
		max_toilet = Constantes.max_toilet_kid;
		max_faim = Constantes.max_faim_kid;
		type = "PLAY";
		ageRange = "Kid";
		this.faim = max_faim;
		this.energy = max_energy;
		this.happiness = max_happiness;
		this.toilet = 10;
		age = 0;
	}
	public void play(Toy t) {
		this.happiness += t.getHappiness();
	}
	@Override
	public void makeSprite() {
		sprite_d = Constantes.spriteDKid;
    	sprite_l = Constantes.spriteLKid;
    	sprite_r = Constantes.spriteRKid;
    	sprite_u = Constantes.spriteUKid;
    	sprite_face = Constantes.spriteFaceKid;
	}
}
