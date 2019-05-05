package model;
import java.awt.image.BufferedImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    protected int sizeW = 1;
    protected int sizeH = 1;
    @JsonIgnore
    protected BufferedImage sprite;

    public GameObject(int X, int Y) {
        this.posX = X;
        this.posY = Y;
        this.makeSprite();
    }
    protected GameObject() {
    	this.makeSprite();
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }
    
    public int getSizeW() {
    	return this.sizeW;
    }
    
    public int getSizeH() {
    	return this.sizeH;
    }
    
    public void setSizeW(int i) {
    	sizeW = i;
    }
    
    public void setSizeH(int i) {
    	sizeH = i;
    }
 
    public boolean isAtPosition(int x, int y) {
        return this.posX == x && this.posY == y;
    }
    public void teleportation(int x, int y) {
    	posX = x;
    	posY = y;
    }
    @JsonIgnore
    public abstract boolean isObstacle();
    public abstract void makeSprite();
    public BufferedImage getSprite() {
    	return sprite;
    }
    public void setPosX(int x) {
    	this.posX = x;
    }
    public void setPosY(int y) {
    	this.posY = y;
    }
}
