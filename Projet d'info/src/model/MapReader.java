package model;

import java.io.IOException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import model.Constantes;
import view.MapDrawer;

public class MapReader {
	private static ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private static ArrayList<Character> Maps = new ArrayList<Character>();
	private static int wTiles;
	private static BlockFactory factory = new BlockFactory();
	private static ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private static Map mapBeingRead;
	public MapReader() {
		}
	private static void makeTiles() {
		for (int i=0; i<Maps.size();i++) {
			int posY = i/wTiles;
			int posX = i%wTiles;
			if (Maps.get(i) == 'A') {
				tiles.add(Constantes.tree);
				objects.add(factory.getInstance("Border", posX , posY, mapBeingRead));
			}
			else if(Maps.get(i) == 'H') {
				tiles.add(Constantes.herb);
			}
			else if(Maps.get(i) == 'D') {
				tiles.add(Constantes.door);
			}
			else if (Maps.get(i) == 'R') {
				tiles.add(Constantes.rock);
			}
			else if (Maps.get(i) == 'S') {
				tiles.add(Constantes.sol);
			}
			else if (Maps.get(i) == 'P') {
				tiles.add(Constantes.path);
			}
			else if (Maps.get(i) == 'C') {
				tiles.add(Constantes.chemin);
			}
			else if (Maps.get(i) == 'W') {
				tiles.add(Constantes.wall);
				objects.add(factory.getInstance("Border", posX , posY, mapBeingRead));
			}
			else if (Maps.get(i) == 's') {
				tiles.add(Constantes.sol2);
			}
			else if (Maps.get(i) == 'w') {
				tiles.add(Constantes.wall2);
				objects.add(factory.getInstance("Border", posX , posY, mapBeingRead));
			}
		}
		mapBeingRead.setObjectsOnMap(objects);
	}
	public static ArrayList<BufferedImage> ReadMap(String s, Map map) {
		objects = new ArrayList<GameObject>();
		int line = 0;
		mapBeingRead = map;
		Maps = new ArrayList<Character>();
		tiles = new ArrayList<BufferedImage>();
		BufferedReader br = null;
		FileReader fr = null;
		try {

			fr = new FileReader(s);
			br = new BufferedReader(fr);

			String sCurrentLine;
			String sizeLine = br.readLine();
			wTiles = Integer.parseInt(sizeLine);
			while ((sCurrentLine = br.readLine()) != null && !(sCurrentLine.isEmpty())) {
				line++;
				if (line <= (2*wTiles)/3) {
					for (int i=0;i<sCurrentLine.length();i++) {
						char aChar = sCurrentLine.charAt(i);
						Maps.add(aChar);
					}
				}
				else if (!(Load.load)) {
					String [] split = sCurrentLine.split(" ");
					int x = Integer.parseInt(split[1]);
					int y = Integer.parseInt(split[2]);
					switch (split[0]) {
					case "house" :
						Building house = new House(x,y);
						mapBeingRead.setHouse((House)house);
						readBuilding(house); break;
					case "market" : 
						Building market = new Market(x,y); 
						readBuilding(market); break;
					case "spa" : 
						Building spa = new Spa(x,y);
						readBuilding(spa); break;
					case "cigaret" : objects.add(factory.getInstance("Cigaret", x , y, mapBeingRead)); break;
					case "apple" : objects.add(factory.getInstance("Apple", x , y, mapBeingRead)); break;
					case "adult" : objects.add(factory.getInstance("Adult", x , y, mapBeingRead)); break;
					case "kid" : objects.add(factory.getInstance("Kid", x , y, mapBeingRead)); break;
					case "teen" : objects.add(factory.getInstance("Teen ", x , y, mapBeingRead)); break;
					case "elder" : objects.add(factory.getInstance("Elder", x , y, mapBeingRead)); break;
					case "toilet" : objects.add(factory.getInstance("Toilet", x , y, mapBeingRead)); break;
					case "kitchen" : objects.add(factory.getInstance("Kitchen", x , y, mapBeingRead)); break;
					case "carpet" : objects.add(factory.getInstance("carpet", x , y, mapBeingRead))/*objects.add(new Image(x,y,"carpet"))*/; break;
					case "stairsDownPart" : objects.add(factory.getInstance("stairsDownPart", x , y, mapBeingRead))/*objects.add(new Image(x,y,"stairsDownPart"))*/; break;
					case "stairsMiddlePart" : objects.add(factory.getInstance("stairsMiddlePart", x , y, mapBeingRead)); break;
					case "stairsUpPart" : objects.add(factory.getInstance("stairsUpPart", x , y, mapBeingRead)); break;
					case "table" : objects.add(factory.getInstance("table", x , y, mapBeingRead)); break;
					case "pancarte1" : objects.add(factory.getInstance("pancarte1", x , y, mapBeingRead)); break;
					case "pancarte50" : objects.add(factory.getInstance("pancarte50", x , y, mapBeingRead)); break;
					case "pancarte10" : objects.add(factory.getInstance("pancarte10", x , y, mapBeingRead)); break;
					case "bath" : objects.add(factory.getInstance("Bath", x , y, mapBeingRead)); break;
					case "dog" : objects.add(factory.getInstance("Dog", x , y, mapBeingRead)); break;
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		try {

			if (br != null)
				br.close();

			if (fr != null)
				fr.close();

		} catch (IOException ex) {

			ex.printStackTrace();

		}
		makeTiles();
		return tiles;
	}
	public static void readBuilding(Building b) {
		for (int i = 0; i< b.getSizeH(); i++) {
			objects.add(new Block(b.getPosX(),i+b.getPosY()));
			objects.add(new Block (b.getPosX()+b.getSizeW()-1,i+b.getPosY()));
		}
		for (int i = 0; i< b.getSizeW(); i++) {
			objects.add(new Block(i+b.getPosX(),b.getPosY()));
			objects.add(new Block (i+b.getPosX(),b.getPosY()+b.getSizeH()-1));
		}
		objects.add(b);
		objects.add(b.getDoor());
	}
	public static int getwTile() {
		return wTiles;
	}
	public static void readWidth(String s) {
		BufferedReader br = null;
		FileReader fr = null;
		try {

			fr = new FileReader(s);
			br = new BufferedReader(fr);

			String sizeLine = br.readLine();
			wTiles = Integer.parseInt(sizeLine);

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		try {

			if (br != null)
				br.close();

			if (fr != null)
				fr.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<BufferedImage> getTiles(){
		return tiles;
	}
}