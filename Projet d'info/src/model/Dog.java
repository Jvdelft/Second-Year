package model;

import java.awt.image.BufferedImage;

public class Dog extends ActivableObject implements Directable{
	protected transient BufferedImage sprite_l = Constantes.dogW;
	protected transient BufferedImage sprite_r = Constantes.dogE;
	protected transient BufferedImage sprite_u = Constantes.dogN;
	protected transient BufferedImage sprite_d = Constantes.dogS;
	public static Dog dogInstance;
	protected int direction = EAST ;
	public Dog(int x,int y) {
		super(x,y);
		dogInstance = this;
	}
	public void move(int X, int Y) {
        this.posX = this.posX + X;
        this.posY = this.posY + Y;
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
    }
	public void setDirection(int direction) {
    	this.direction = direction;
    }
	public BufferedImage getSprite() {
    	BufferedImage sprite = null;
    	switch(this.getDirection()) {
    	case (EAST) : sprite = this.sprite_r; break;
    	case(WEST) : sprite = this.sprite_l; break;
    	case(NORTH) : sprite = this.sprite_u; break;
    	case(SOUTH) : sprite = this.sprite_d; break;
    	}
    	return sprite;
    }
	public int getFrontX() {
        int delta = 0;
        if (direction % 2 == 0){
            delta += 1 - direction;
        }
        return this.posX + delta;
    }
    public int getFrontY() {
        int delta = 0;
        if (direction % 2 != 0){
            delta += direction - 2;
        }
        return this.posY + delta;
    }
    public boolean isPlayable() {
		return false;
	}
    @Override
	public void activate(Sums s) {
		
	}
	@Override
	public boolean isObstacle() {
		return false;
	}
	@Override
	public void makeSprite() {
	}
	@Override
	public int getDirection() {
		return direction;
	}
	
}
