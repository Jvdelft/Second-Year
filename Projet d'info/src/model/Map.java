package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import view.Window;

public class Map {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private int sizeW;
	private int sizeH;
	private int tileSize;
	private boolean [][] positionTaken;
	private String mapName;
	private ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	public Map(String path) {
		this.mapName = path;
		tiles = MapReader.ReadMap(path,this);
		sizeW = MapReader.getwTile(); //problème avec pixels 
		sizeH = (sizeW * 2)/3 ;
		tileSize = 1440 / sizeW;
		initMap();
		
		
    }
	public void initMap() {
		House h = new House(20,1);
		if (mapName.equals(Constantes.mapBase)) {
	    	Sums p = new Adult(10, 10,h);
	    	Sums q = new Kid(5,5,h);
	    	Sums r = new Elder(15,15,h);
	    	Sums s = new Teen(24,6,h);
	    	Market m = new Market(4,2);
	    	
	    	this.addObject(h);
	    	this.addObject(p);
	    	this.addObject(q);
	    	this.addObject(r);
	    	this.addObject(s);
	    	this.addObject(m);
	    	Cigaret c = new Cigaret(13,15);
	    	this.addObject(c);
	    	this.addObject(new Apple(7,7));
	    	ArrayList <Building> building = new ArrayList<Building>();
	    	building.add(h);
	    	building.add(m);
			for (Building b : building) {
				for (int i = 0; i< b.getSizeH(); i++) {
					this.addObject(new Border(b.getPosX(),i+b.getPosY()));
					this.addObject(new Border (b.getPosX()+b.getSizeW()-1,i+b.getPosY()));
				}
				for (int i = 0; i< b.getSizeH(); i++) {
					this.addObject(new Border(i+b.getPosX(),b.getPosY()));
					this.addObject(new Border (i+b.getPosX(),b.getPosY()+b.getSizeH()-1));
				}
				this.addObject(b.getDoor());
			}
			
	    	this.addObject(new Door(Math.round(sizeW/2),0, Constantes.mapRock, 'S'));
	    	this.addObject(new Door(0,Math.round(sizeH/2)-1, Constantes.mapRock, 'E'));
	    	this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapRock, 'N'));
	    	this.addObject(new Door(sizeW-1,Math.round(sizeH/2)-1, Constantes.mapRock, 'W'));
		}
		else if (mapName.equals(Constantes.mapRock)) { //modifier sizeW, sizeH en fonction de la taille de la map
	    	this.addObject(new Door(Math.round(sizeW/2),0, Constantes.mapBase, 'S'));
	    	this.addObject(new Door(0,Math.round(sizeH/2)-1, Constantes.mapBase, 'E'));
	    	this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'N'));
	    	this.addObject(new Door(sizeW-1,Math.round(sizeH/2)-1, Constantes.mapBase, 'W'));
	    	System.out.println("Chargement map rock");

		}
		else if (mapName.equals(Constantes.mapMaison)) {
			this.addObject(new Adult(10,4,h));
			this.addObject(new Fridge(10,1));
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			this.addObject(new Toilet(Math.round(sizeW/2), 1));
			this.addObject(new Sofa(1,Math.round(sizeH/2), 0));
			this.addObject(new Toy(10,7));
			System.out.println("Chargement MapMaison"); 
		}
		else if (mapName.equals(Constantes.mapMarket)) {
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'M'));
			Fridge f = new Fridge(2, 1);
			Fridge f2 = new Fridge(3, 1);
			Fridge f3 = new Fridge(1,2);
			Fridge f4 = new Fridge(1,3);
			this.addObject(f);
			this.addObject(f2);
			this.addObject(f3);
			this.addObject(f4);
			System.out.println("Chargement MapMarket"); 
		}
		
		Window.getInstance().update();
	}
	public void addObject(GameObject o) {
		objects.add(o);
	}
	public int getSizeW() {
		return sizeW;
	}
	public int getSizeH() {
		return sizeH;
	}
	public int getTileSize() {
		return tileSize;
	}
	public ArrayList<BufferedImage> getTiles(){
		return tiles;
	}
	public ArrayList<GameObject> getObjects(){
		return objects;
	}
	public ArrayList<ActivableObject> getActivableObjects(){
		ArrayList<ActivableObject> res = new ArrayList<ActivableObject>();
		for (GameObject object : objects) {
			if (object instanceof ActivableObject) { res.add((ActivableObject)object);}
		}
		return res;
	}
	public void setObjects(ArrayList<GameObject> o) {
		if (o != null) {
			objects = o;
		}
	}
}
