package model;

import java.awt.image.BufferedImage;

public class Dog extends ActivableObject implements Directable{
	protected transient BufferedImage sprite_l;
	protected transient BufferedImage sprite_r;
	protected transient BufferedImage sprite_u;
	protected transient BufferedImage sprite_d;
	protected int direction = EAST ;
	public Dog(int x,int y) {
		super(x,y);
		type = "PET";
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
	public int getFrontX() {		//on récupère la position en face du personnage.
        int delta = 0;
        if (direction % 2 == 0){
            delta += 1 - direction;
        }
        return this.posX + delta;
    }
    public int getFrontY() {		//on récupère la position en face du personnage.
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
    @Override
	public void activate(Sums s) {
		s.setHappiness(s.getHappiness()+10);
	}
	@Override
	public boolean isObstacle() {
		return false;
	}
	@Override
	public void makeSprite() {
		sprite_l = Constantes.dogW;
		sprite_r = Constantes.dogE;
		sprite_u = Constantes.dogN;
		sprite_d = Constantes.dogS;
	}
	@Override
	public int getDirection() {
		return direction;
	}
	
}
