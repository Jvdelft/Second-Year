package model;
import java.awt.image.BufferedImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    @JsonIgnore
    public BufferedImage Sprite;

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
}
