package model;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

public abstract class GameObject implements Serializable{
    protected int posX;
    protected int posY;
    @JsonIgnore
    protected int sizeW = 1;
    @JsonIgnore
    protected int sizeH = 1;
    @JsonIgnore
    protected int price = 0;
    @JsonIgnore
    protected transient BufferedImage sprite;

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
    
    public int getPrice() {
    	return price;
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
    @JsonIgnore
    public abstract void makeSprite();
    @JsonIgnore
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
