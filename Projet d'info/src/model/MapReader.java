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
		objects = new ArrayList<GameObject>();
		for (int i=0; i<Maps.size();i++) {
			int posY = i/wTiles;
			int posX = i%wTiles;
			if (Maps.get(i) == 'A') {
				tiles.add(Constantes.tree);
				objects.add(factory.getInstance("Border", posX , posY));
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
				objects.add(factory.getInstance("Border", posX , posY));
			}
			else if (Maps.get(i) == 's') {
				tiles.add(Constantes.sol2);
			}
			else if (Maps.get(i) == 'w') {
				tiles.add(Constantes.wall2);
				objects.add(factory.getInstance("Border", posX , posY));
			}
		}
			mapBeingRead.setObjects(objects);
	}
	public static ArrayList<BufferedImage> ReadMap(String s, Map map) {
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
			while ((sCurrentLine = br.readLine()) != null) {
				for (int i=0;i<sCurrentLine.length();i++) {
					char aChar = sCurrentLine.charAt(i);
					Maps.add(aChar);
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