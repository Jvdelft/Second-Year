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
	public static int image_size = 70;
	public static BufferedImage Tree;
	public static BufferedImage Herb;
	public static BufferedImage Door;
	public static BufferedImage Rock;
	public static BufferedImage House;
	public static BufferedImage Sol;
	public static BufferedImage Wall;
	public static BufferedImage Path;
	public static BufferedImage Chemin;
	public static BufferedImage Background;
	public static BufferedImage BackgroundStatus;
	public static BufferedImage Menu;
	public static BufferedImage arrowUp;
	public static BufferedImage arrowDown;
	public static BufferedImage inventoryCase;
	public static BufferedImage Apple;
	public static ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public static HashMap imageHashMap = new HashMap(); 
	public static BufferedImage fridge;
	public static int itemsNumber = 4;
	public static void Images() {
		try {
			Tree = ImageIO.read(new File("Images/tree.png"));
			Herb = ImageIO.read(new File("Images/ground.png"));
			Door = ImageIO.read(new File("Images/Door.png"));
			imageList.add(Door);
			Rock = ImageIO.read(new File("Images/Rock.png"));
			House = ImageIO.read(new File("Images/maison.jpg"));
			Sol = ImageIO.read(new File("Images/sol.png"));
			Path = ImageIO.read(new File("Images/Stone_Floor.png"));
			Chemin = ImageIO.read(new File("Images/Dirt_Path.jpg"));
			Background = ImageIO.read(new File("Images/background.jpg"));
			BackgroundStatus = ImageIO.read(new File("Images/BackgroundStatus.jpg"));
			Menu = ImageIO.read(new File("Images/Menu.jpg"));
			arrowUp = ImageIO.read(new File("Images/arrowup.png"));
			imageList.add(arrowUp);
			arrowDown = ImageIO.read(new File("Images/arrowdown.png"));
			imageList.add(arrowDown);
			inventoryCase = ImageIO.read(new File("Images/inventorycase.png"));
			Apple = ImageIO.read(new File("Images/Apple.png"));
			imageList.add(Apple);
			fridge = ImageIO.read(new File("Images/Fridge.png"));
			
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
