package model;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class GameObject implements Serializable{
    protected int posX;
    protected int posY;
    protected int sizeW = 1;	//Les size décideront sur combien de cases le Sprite sera dessiné.
    protected int sizeH = 1;
    protected int price = 0;	//Le prix est fixé à 0 car dans les containers hors du marché ça ne coûte rien de stocker des objets.
    protected transient BufferedImage sprite;

    public GameObject(int X, int Y) {
        this.posX = X;
        this.posY = Y;
        this.makeSprite();
    }
    protected GameObject() {		//2 Constructeurs pour pouvoir définir des objets sans position fixe notamment pour les inventaires.
    	this.makeSprite();
    }
    public boolean isAtPosition(int x, int y) {
        return this.posX == x && this.posY == y;
    }
    public void teleportation(int x, int y) {
    	posX = x;
    	posY = y;
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
