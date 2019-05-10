package model;

import java.io.File; 
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Food extends DeletableObject {
    protected int NutritionalValue;
	protected int EnergyValue;
	protected Food(int X, int Y) {
		super(X, Y);
		type = "EAT";

	}
	public Food() {
		super();
		type = "EAT";
	}
	public int getEnergyValue() {
		return EnergyValue;
	}
	public void setEnergyValue(int v) {
		this.EnergyValue = v;
	}
	public int getNutritionalValue() {
		return NutritionalValue;
	}
	public void setNutritionalValue(int n) {
		this.NutritionalValue = n;
	}
	@Override
	public void activate (Sums s) {
		s.Eat(this);
		new Thread(new Sound("Eat_Toast", 0.25, 2500)).start();
		notifyDeletableObserver();
	}
	@Override
	public boolean isObstacle() {
	   return true;
	}
		
		
}
