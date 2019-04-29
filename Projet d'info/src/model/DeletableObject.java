package model;

import java.util.ArrayList;

public abstract class DeletableObject extends ActivableObject implements Deletable{

    private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
    private int LifePoint;
    protected DeletableObject (int X, int Y, int LifePoint) {
        super(X, Y); 
        this.LifePoint = LifePoint;
    }
    public DeletableObject() {
    	super();
    }
    public abstract void activate(Sums s);
    public int getLifePoint() {
    	return LifePoint;
    }
    

    // //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void attachDeletable(DeletableObserver po) {
        observers.add(po);
    }

    @Override
    public void notifyDeletableObserver() {
        int i = 0;
        this.LifePoint -= 1;
        if (LifePoint <= 0) {
        	for (DeletableObserver o : observers) {
                i++;
                o.delete(this, null);
        	}
        }
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

}
