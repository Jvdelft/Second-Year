package model;

import java.awt.image.BufferedImage;

public class Image extends GameObject{
	String image;
	protected BufferedImage spriteHouse2;
	protected BufferedImage spriteCarpet;
	public Image(String s) {
		image = s;
		configureImage();
	}
	public Image(int x, int y, String s) {
		super(x,y);
		image = s;
		configureImage();
	}
	private void configureImage() {
		switch (image) {
		case "house2" :
			price = 50; break;
		case "carpet" : 
			sizeH = 3;
			sizeW = 4; break;
		}
		
	}
	public void makeSprite() {
		spriteHouse2=Constantes.house2;
		spriteCarpet=Constantes.carpet;
	}
	public BufferedImage getSprite() {
		BufferedImage sprite = null;
		switch(image) {
		case "house2" : sprite = spriteHouse2; break;
		case "carpet" : sprite = spriteCarpet; break;
		}
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