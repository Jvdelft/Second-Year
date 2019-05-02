package model;

import java.io.IOException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import model.Constantes;
import view.Map;

public class MapReader {
	public ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private ArrayList<Character> Maps = new ArrayList<Character>();
	private int wTiles;
	private BlockFactory factory = new BlockFactory();
	public MapReader() {
		Constantes.Images();
		}
	public void makeTiles() {
		for (int i=0; i<Maps.size();i++) {
			int posY = i/wTiles;
			int posX = i%wTiles;
			if (Maps.get(i) == 'A') {
				tiles.add(Constantes.tree);
				factory.getInstance("Border", posX , posY);
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
				factory.getInstance("Border", posX , posY);
			}
		}
	}
	public void ReadMap(String s) {
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
		Map.getInstance().changeMapSize();
		makeTiles();
	}
	public int getwTile() {
		return wTiles;
	}
	public void readWidth(String s) {
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
		Map.getInstance().changeMapSize();
}
}