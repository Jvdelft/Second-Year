package model;

import java.awt.image.BufferedImage;

public class Image extends GameObject{
	String image;
	protected BufferedImage sprite1;
	protected BufferedImage sprite2;
	public Image(String s) {
		image = s;
		if (image=="House2") {price=50;}
	}
	
	public void makeSprite() {
		sprite1=Constantes.house2;
		//sprite2=Constantes.house3;
	}
	public BufferedImage getSprite() {
		BufferedImage sprite = null;
		if (image=="House2") {sprite=sprite1;}
		else if (image=="House3") {sprite=sprite2;}
		return sprite;
	}
	@Override
	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getImage() {
		return image;
	}

}