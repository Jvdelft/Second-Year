package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Elder extends Sums{
	public Elder(int x, int y, House h) {		//on mets les besoins au maximum lors de la création du personnage.
		super(x,y,h);
		max_energy = Constantes.max_energy_elder;
		max_faim = Constantes.max_faim_elder;
		max_toilet = Constantes.max_toilet_elder;
		max_happiness = Constantes.max_happiness_elder;
		ageRange = "Elder";
		this.faim = max_faim;
		this.energy = max_energy;
		this.happiness = max_happiness;
		this.toilet = 10;
		age = 60;
		}
	@Override
	public void makeSprite() {
		sprite_d = Constantes.spriteDElder;
    	sprite_l = Constantes.spriteLElder;
    	sprite_r = Constantes.spriteRElder;
    	sprite_u = Constantes.spriteUElder;
    	sprite_face = Constantes.spriteFaceElder;
	}
}
