package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Constantes {
	public static final String mapBase = "Maps/MapBase.txt";
	public static final String mapRock = "Maps/MapRock.txt";
	public static final String mapMaison = "Maps/MapMaison.txt";
	public static final String mapMarket = "Maps/MapMarket.txt";
	public static final int image_size = 70;
	public static BufferedImage tree;
	public static BufferedImage herb;
	public static BufferedImage door;
	public static BufferedImage rock;
	public static BufferedImage house;
	public static BufferedImage market;
	public static BufferedImage sol;
	public static BufferedImage wall;
	public static BufferedImage path;
	public static BufferedImage chemin;
	public static BufferedImage background;
	public static BufferedImage backgroundStatus;
	public static BufferedImage menu;
	public static BufferedImage arrowUp;
	public static BufferedImage arrowDown;
	public static BufferedImage inventoryCase;
	public static BufferedImage apple;
	public static BufferedImage cigaret;
	public static ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public static HashMap imageHashMap = new HashMap(); 
	public static BufferedImage fridge;
	public static final int itemsNumber = 4;
	public static BufferedImage toilet;
	public static BufferedImage bubbleThought;
	public static BufferedImage coin;
	public static BufferedImage sofaWest;
	public static BufferedImage sofaEast;
	public static BufferedImage sofaNorth;
	public static BufferedImage sofaSouth;
	public static final String menuInGame = "Images/forest.gif";
	public static final int max_faim_kid = 80;
	public static final int max_happiness_kid = 200;
	public static final int max_toilet_kid = 75;
	public static final int max_energy_kid = 200;
	public static final int max_faim_elder = 100;
	public static final int max_happiness_elder = 100;
	public static final int max_toilet_elder = 50;
	public static final int max_energy_elder = 50;
	public static final int max_faim_adult = 100;
	public static final int max_happiness_adult = 100;
	public static final int max_toilet_adult = 100;
	public static final int max_energy_adult = 100;
	public static final int max_faim_teen = 120;
	public static final int max_happiness_teen = 50;
	public static final int max_toilet_teen = 100;
	public static final int max_energy_teen = 75;
	public static BufferedImage spriteUAdult;
	public static BufferedImage spriteDAdult;
	public static BufferedImage spriteLAdult;
	public static BufferedImage spriteRAdult;
	public static BufferedImage spriteFaceAdult;
	public static BufferedImage spriteUKid;
	public static BufferedImage spriteDKid;
	public static BufferedImage spriteLKid;
	public static BufferedImage spriteRKid;
	public static BufferedImage spriteFaceKid;
	public static BufferedImage spriteUElder;
	public static BufferedImage spriteDElder;
	public static BufferedImage spriteLElder;
	public static BufferedImage spriteRElder;
	public static BufferedImage spriteFaceElder;
	public static BufferedImage spriteUTeen;
	public static BufferedImage spriteDTeen;
	public static BufferedImage spriteLTeen;
	public static BufferedImage spriteRTeen;
	public static BufferedImage spriteFaceTeen;
	public static BufferedImage bedOne;
	public static void Images() {
		try {
			tree = ImageIO.read(new File("Images/tree.png"));
			herb = ImageIO.read(new File("Images/ground.png"));
			door = ImageIO.read(new File("Images/Door.png"));
			rock = ImageIO.read(new File("Images/Rock.png"));
			house = ImageIO.read(new File("Images/House.png"));
			market = ImageIO.read(new File("Images/market.png"));
			sol = ImageIO.read(new File("Images/sol.png"));
			path = ImageIO.read(new File("Images/Stone_Floor.png"));
			chemin = ImageIO.read(new File("Images/Dirt_Path.jpg"));
			background = ImageIO.read(new File("Images/background.jpg"));
			backgroundStatus = ImageIO.read(new File("Images/BackgroundStatus.jpg"));
			menu = ImageIO.read(new File("Images/Menu.jpg"));
			arrowUp = ImageIO.read(new File("Images/arrowup.png"));
			imageList.add(arrowUp);
			arrowDown = ImageIO.read(new File("Images/arrowdown.png"));
			imageList.add(arrowDown);
			inventoryCase = ImageIO.read(new File("Images/inventorycase.png"));
			apple = ImageIO.read(new File("Images/Apple.png"));
			cigaret = ImageIO.read(new File("Images/cigaret.png"));
			imageList.add(apple);
			imageList.add(cigaret);
			fridge = ImageIO.read(new File("Images/Fridge.png"));
			toilet = ImageIO.read(new File("Images/toilet.png"));
			bubbleThought = ImageIO.read(new File("Images/bubbleThought.png"));
			coin = ImageIO.read(new File("Images/coin.png"));
			sofaSouth = ImageIO.read(new File("Images/sofaSouth.png"));
			sofaNorth = ImageIO.read(new File("Images/sofaNorth.png"));
			sofaEast = ImageIO.read(new File("Images/sofaEast.png"));
			sofaWest = ImageIO.read(new File("Images/sofaWest.png"));
			spriteDAdult = ImageIO.read(new File("Images/Sprites Man/image_part_056.png"));
    		spriteLAdult = ImageIO.read(new File("Images/Sprites Man/image_part_068.png"));
    		spriteRAdult = ImageIO.read(new File("Images/Sprites Man/image_part_080.png"));
    		spriteUAdult = ImageIO.read(new File("Images/Sprites Man/image_part_092.png"));
    		spriteFaceAdult = ImageIO.read(new File("Images/Sprite Man Face/0.png"));
    		spriteDElder = ImageIO.read(new File("Images/Sprites Old Man/0.png"));
    		spriteLElder = ImageIO.read(new File("Images/Sprites Old Man/12.png"));
    		spriteRElder = ImageIO.read(new File("Images/Sprites Old Man/24.png"));
    		spriteUElder = ImageIO.read(new File("Images/Sprites Old Man/36.png"));
    		spriteFaceElder = ImageIO.read(new File("Images/Sprites Old Man Face/0.png"));
    		spriteDKid = ImageIO.read(new File("Images/Sprites Kid M/52.png"));
    		spriteLKid = ImageIO.read(new File("Images/Sprites Kid M/64.png"));
    		spriteRKid = ImageIO.read(new File("Images/Sprites Kid M/76.png"));
    		spriteUKid = ImageIO.read(new File("Images/Sprites Kid M/88.png"));
    		spriteFaceKid = ImageIO.read(new File("Images/Sprites Kid M Face/1.png"));
    		spriteDTeen = ImageIO.read(new File("Images/Sprites Teen M/2.png"));
    		spriteLTeen = ImageIO.read(new File("Images/Sprites Teen M/5.png"));
    		spriteRTeen = ImageIO.read(new File("Images/Sprites Teen M/8.png"));
    		spriteUTeen= ImageIO.read(new File("Images/Sprites Teen M/11.png"));
    		spriteFaceTeen = ImageIO.read(new File("Images/Sprite Teen M Face/1.png"));
    		bedOne = ImageIO.read(new File("Images/bed1.png"));
			wall = ImageIO.read(new File("Images/wall.jpg"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void makeList() {
		for (int i = 0; i < imageList.size(); i++) {
			Image img = imageList.get(i).getScaledInstance(image_size, image_size, Image.SCALE_FAST);
			imageHashMap.put(imageList.get(i), img);
		}
	}
}
