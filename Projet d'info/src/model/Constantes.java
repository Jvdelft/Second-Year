package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Constantes {
	public static String MapBase = "Maps/MapBase.txt";
	public static String MapRock = "Maps/MapRock.txt";
	public static String MapMaison = "Maps/MapMaison.txt";
	public static String MapMarket = "Maps/MapMarket.txt";
	public static int image_size = 70;
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
	public static ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public static HashMap imageHashMap = new HashMap(); 
	public static BufferedImage fridge;
	public static int itemsNumber = 4;
	public static BufferedImage toilet;
	public static BufferedImage bubbleThought;
	public static BufferedImage coin;
	public static BufferedImage sofaWest;
	public static BufferedImage sofaEast;
	public static BufferedImage sofaNorth;
	public static BufferedImage sofaSouth;
	public static String menuInGame = "Images/forest.gif";
	public static int max_faim_kid = 80;
	public static int max_happiness_kid = 200;
	public static int max_toilet_kid = 75;
	public static int max_energy_kid = 200;
	public static int max_faim_elder = 100;
	public static int max_happiness_elder = 100;
	public static int max_toilet_elder = 50;
	public static int max_energy_elder = 50;
	public static int max_faim_adult = 100;
	public static int max_happiness_adult = 100;
	public static int max_toilet_adult = 100;
	public static int max_energy_adult = 100;
	public static int max_faim_teen = 120;
	public static int max_happiness_teen = 50;
	public static int max_toilet_teen = 100;
	public static int max_energy_teen = 75;
	public static void Images() {
		try {
			tree = ImageIO.read(new File("Images/tree.png"));
			herb = ImageIO.read(new File("Images/ground.png"));
			door = ImageIO.read(new File("Images/Door.png"));
			imageList.add(door);
			rock = ImageIO.read(new File("Images/Rock.png"));
			house = ImageIO.read(new File("Images/maison.jpg"));
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
			imageList.add(apple);
			fridge = ImageIO.read(new File("Images/Fridge.png"));
			toilet = ImageIO.read(new File("Images/toilet.png"));
			bubbleThought = ImageIO.read(new File("Images/bubbleThought.png"));
			coin = ImageIO.read(new File("Images/coin.png"));
			sofaSouth = ImageIO.read(new File("Images/sofaSouth.png"));
			sofaNorth = ImageIO.read(new File("Images/sofaNorth.png"));
			sofaEast = ImageIO.read(new File("Images/sofaEast.png"));
			sofaWest = ImageIO.read(new File("Images/sofaWest.png"));
			
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
