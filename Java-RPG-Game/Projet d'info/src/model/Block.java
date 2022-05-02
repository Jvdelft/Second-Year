package model;

import java.awt.image.BufferedImage;

public class Block extends GameObject{
	private String type = "";
	protected transient BufferedImage sprite1;
	protected transient BufferedImage sprite2;
	protected transient BufferedImage sprite5;
	protected transient BufferedImage sprite10;
	protected transient BufferedImage sprite20;
	protected transient BufferedImage sprite50;
	protected transient BufferedImage sprite100;
	protected transient BufferedImage sprite200;
	protected transient BufferedImage sprite500;
	protected transient BufferedImage sprite1000;
	protected transient BufferedImage spriteTable;
	public Block(int x, int y, String type) {	//les blocks constituent tous les objets de décoration mais bloquant le passage.
		super(x,y);
		this.type = type;
	}
	public Block(int x, int y) {		//2 constructeurs différents sont nécessaires car on peut instancier des block classiques formant les bords de la map ou
		super(x,y);						//des blocks particuliers comme des tables ou des pancartes qui ont des sprites particuliers.
	}
	@Override
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
		case "table" : sprite = spriteTable; break;
		}
		return sprite;
	}
	@Override
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
		spriteTable=Constantes.table;
	}
	@Override
	public boolean isObstacle() {
		return true;
	}

}