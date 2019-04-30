package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Food extends ActivableObject implements Deletable{
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
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
    public int getLifePoint() {
    	return LifePoints;
    }
    public void setLifePoint(int l) {
    	this.LifePoints = l;
    }
    

    // //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void attachDeletable(DeletableObserver po) {
        observers.add(po);
    }

    @Override
    public void notifyDeletableObserver() {
        this.LifePoints -= 1;
        if (LifePoints <= 0) {
        	for (DeletableObserver o : observers) {
                o.delete(this, null);
        	}
        }
    }

    @Override
    public boolean isObstacle() {
        return true;
    }
	
	
	public void activate (Sums s) {
		s.Eat(this);
		new Thread(new Sound("Eat_Toast")).start();
		notifyDeletableObserver();
		
		
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
}
