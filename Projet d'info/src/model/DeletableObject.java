package model;

import java.util.ArrayList;

public abstract class DeletableObject extends ActivableObject implements Deletable{

    private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
    protected DeletableObject (int X, int Y) {
        super(X, Y); 
        lifePoints = 1;
    }
    public DeletableObject() {
    	super();
    }
	public int getLifePoints() {
		return this.lifePoints;
	}
	public void setLifePoints(int l) {
		this.lifePoints = l;
	}
    // //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void attachDeletable(DeletableObserver po) {
    	if (observers.size() == 0) {
    		observers.add(po);
    	}
    	else if (po instanceof Sums) { observers.add(po);}
    }

    //@Override
    public void notifyDeletableObserver() {
        this.lifePoints -= 1;
        if (lifePoints <= 0) {
        	for (DeletableObserver o : observers) {
                o.delete(this);
        	}
        }
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

}
