package model;

import java.awt.image.BufferedImage;

public class Block extends GameObject{
	private String type = "";
	protected BufferedImage sprite1;
	protected BufferedImage sprite2;
	protected BufferedImage sprite5;
	protected BufferedImage sprite10;
	protected BufferedImage sprite20;
	protected BufferedImage sprite50;
	protected BufferedImage sprite100;
	protected BufferedImage sprite200;
	protected BufferedImage sprite500;
	protected BufferedImage sprite1000;
	public Block(int x, int y, String type) {
		super(x,y);
		this.type = type;
	}
	public Block(int x, int y) {
		super(x,y);
	}
	public Block() {
		super();
	}
	public BufferedImage getSprite() {
		BufferedImage sprite = null;
		switch (type) {
		case "pancarte1" : sprite = sprite1; break;
		case "pancarte2" : sprite = sprite2; break;
		case "pancarte5" : sprite = sprite5; break;
		case "pancarte10" : sprite = sprite10; break;
		case "pancarte20" : sprite = sprite20; break;
		case "pancarte50" : sprite = sprite50; break;
		case "pancarte100" : sprite = sprite100; break;
		case "pancarte200" : sprite = sprite200; break;
		case "pancarte500" : sprite = sprite500; break;
		case "pancarte1000" : sprite = sprite1000; break;
		}
		return sprite;
	}
	public void makeSprite() { 
		sprite1 = Constantes.pancarte1;
		sprite2 = Constantes.pancarte2;
		sprite5 = Constantes.pancarte5;
		sprite10 = Constantes.pancarte10;
		sprite20 = Constantes.pancarte20;
		sprite50 = Constantes.pancarte50;
		sprite100 = Constantes.pancarte100;
		sprite200 = Constantes.pancarte200;
		sprite500 = Constantes.pancarte500;
		sprite1000 = Constantes.pancarte1000;
	}
	public boolean isObstacle() {
		return true;
	}

}