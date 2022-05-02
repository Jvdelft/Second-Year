package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import view.Window;

public class Adult extends Sums {
	public Adult(int x , int y,House h) {
		super(x,y,h);
		max_energy = Constantes.max_energy_adult;  //les différents besoins des Sums sont remis au maximum lors de leur création.
		max_faim = Constantes.max_faim_adult;
		max_toilet = Constantes.max_toilet_adult;
		max_happiness = Constantes.max_happiness_adult;
		type = "other";
		typeAffection = "MAKE LOVE";
		ageRange = "Adult";			
		user = "Adult";			//Seuls les adultes peuvent se séduire et avoir des enfants.
		this.faim = max_faim;
		this.energy = max_energy;
		this.happiness = max_happiness;
		this.toilet = 10;
		age = 21;
		}
	public void work() {
		this.setEnergy((int) (this.getEnergy()-70));  //il faut passer par les getters et les setters pour s'assurer que la valeur maximale n'est pas dépassée.
		maison.changeMoney(2000); 
	}
	public void receiveFlower(Sums s) {
		this.interraction(s, 10);
    }
	public void makeLove() {
		Game.getInstance().makeBaby(this.getHouse());
	}
	@Override
	public void makeSprite() {
    	sprite_d = Constantes.spriteDAdult;
    	sprite_l = Constantes.spriteLAdult;
    	sprite_r = Constantes.spriteRAdult;
    	sprite_u = Constantes.spriteUAdult;
    	sprite_face = Constantes.spriteFaceAdult;
	}
}
