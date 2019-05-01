package model;

public class Sofa extends ActivableObject implements Directable{
	private int direction;
	public Sofa(int x, int y, int direction) {
		super(x,y);
		this.direction = direction;
	}
	public Sofa() {
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

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		switch (direction) {
		case NORTH : Sprite = Constantes.sofaNorth; break;
		case EAST : Sprite = Constantes.sofaEast; break;
		case WEST : Sprite = Constantes.sofaWest; break;
		case SOUTH : Sprite = Constantes.sofaSouth; break;
		}
	}

	@Override
	public int getDirection() {
		return 0;
	}

}
