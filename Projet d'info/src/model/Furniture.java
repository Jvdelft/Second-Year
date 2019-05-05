package model;

public abstract class Furniture extends ActivableObject implements Directable{
	protected int direction;
	public Furniture(int x, int y) {
		super(x,y);
	}
	public Furniture() {
		super();
	}
	public void rotate(int x, int y) {
        if(x == 0 && y == -1)
            direction = NORTH;
        else if(x == 0 && y == 1)
            direction = SOUTH;
        else if(x == 1 && y == 0)
            direction = EAST;
        else if(x == -1 && y == 0)
            direction = WEST;
        makeSprite();
    }

}
