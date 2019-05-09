package model;

import java.awt.image.BufferedImage;

public class ImageDraw extends GameObject{
	String image;
	protected transient BufferedImage spriteHouse2;
	protected transient BufferedImage spriteCarpet;
	protected transient BufferedImage spriteStairsDownPart;
	protected transient BufferedImage spriteStairsMiddlePart;
	protected transient BufferedImage spriteStairsUpPart;
	public ImageDraw(String s) {
		image = s;
		configureImage();
	}
	public ImageDraw(int x, int y, String s) {
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
		spriteStairsDownPart = Constantes.stairsDownPart;
		spriteStairsMiddlePart = Constantes.stairsMiddlePart;
		spriteStairsUpPart = Constantes.stairsUpPart;
	}
	public BufferedImage getSprite() {
		BufferedImage sprite = null;
		switch(image) {
		case "house2" : sprite = spriteHouse2; break;
		case "carpet" : sprite = spriteCarpet; break;
		case "stairsDownPart" : sprite = spriteStairsDownPart; break;
		case "stairsMiddlePart" : sprite = spriteStairsMiddlePart; break;
		case "stairsUpPart" : sprite = spriteStairsUpPart; break;
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