package model;

import java.util.ArrayList;

public abstract class DeletableObject extends ActivableObject implements Deletable{

    private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
    protected int LifePoint = 1;
    protected DeletableObject (int X, int Y) {
        super(X, Y); 
    }
    public DeletableObject() {
    	super();
    }
    public int getLifePoint() {
    	return LifePoint;
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
        this.LifePoint -= 1;
        if (LifePoint <= 0) {
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
