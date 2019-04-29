package model;

import java.util.ArrayList;

public class Drinks extends ActivableObject implements Deletable{
	protected int EnergyValue;
	protected int Toilet;
	protected int Value;
	protected int LifePoint;
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
	
	public Drinks(int X, int Y) {
		super(X, Y);
	}
	public Drinks() {
		super();
	}
	public void activate (Sums s) {
		s.Drink(this);
		notifyDeletableObserver();
	}
	public int getValue() {
		return Value;
	}
	public int getEnergyValue() {
		return EnergyValue;
	}
	public int getLifePoint() {
		return LifePoint;
	}
	public int MakeUPee() {
		return Toilet;
	}
	// //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void attachDeletable(DeletableObserver po) {
        observers.add(po);
    }

    @Override
    public void notifyDeletableObserver() {
        this.LifePoint -= 1;
        if (LifePoint <= 0) {
        	for (DeletableObserver o : observers) {
                o.delete(this, null);
        	}
        }
    }

    @Override
    public boolean isObstacle() {
        return true;
    }
    public void makeSprite() {
    }

}
