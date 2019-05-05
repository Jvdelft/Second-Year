package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import controller.Keyboard;
import view.HUD;
import view.MapDrawer;
import view.Window;

public class Map {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private int sizeW;
	private int sizeH;
	private int tileSize;
	private boolean positionTaken[][];
	private String mapName;
	private ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private boolean notInitHouse = true;
	private ArrayList<GameObject> objectsToPlace = new ArrayList<GameObject>();
	private MapDrawer mapDrawer = MapDrawer.getInstance();
	private GameObject lastObjectPlaced = null;
	public Map(String path) {
		this.mapName = path;
		MapReader.readWidth(path);
		sizeW = MapReader.getwTile(); //problème avec pixels 
		sizeH = (sizeW * 2)/3 ;
		tileSize = 1440 / sizeW;
		positionTaken = new boolean[sizeW][sizeH];
		tiles = MapReader.ReadMap(path,this);
		initMap();
		
		
    }
	public void initMap() {
		House h = new House(20,1);
		if (mapName.equals(Constantes.mapBase)) {
	    	Sums p = new Adult(10, 9,h);
	    	Sums q = new Kid(5,6,h);
	    	Sums r = new Elder(15,15,h);
	    	Sums s = new Teen(24,6,h);
	    	Market m = new Market(4,2);
	    	Spa spa = new Spa(4, 10);
	    	
	    	this.addObject(h);
	    	this.addObject(p);
	    	this.addObject(q);
	    	this.addObject(r);
	    	this.addObject(s);
	    	this.addObject(m);
	    	this.addObject(spa);
	    	Cigaret c = new Cigaret(13,15);
	    	this.addObject(c);
	    	this.addObject(new Apple(7,7));
	    	ArrayList <Building> building = new ArrayList<Building>();
	    	building.add(h);
	    	building.add(m);
	    	building.add(spa);
			for (Building b : building) {
				for (int i = 0; i< b.getSizeH(); i++) {
					this.addObject(new Block(b.getPosX(),i+b.getPosY()));
					this.addObject(new Block (b.getPosX()+b.getSizeW()-1,i+b.getPosY()));
				}
				for (int i = 0; i< b.getSizeW(); i++) {
					this.addObject(new Block(i+b.getPosX(),b.getPosY()));
					this.addObject(new Block (i+b.getPosX(),b.getPosY()+b.getSizeH()-1));
				}
				objects.add(b.getDoor());
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
			objectsToPlace.add(new Fridge());
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			this.addObject(new Toilet(1,1));
			objectsToPlace.add(new Sofa(1,Math.round(sizeH/2), 0));
			objectsToPlace.add(new Toy(10,7));
			System.out.println("Chargement MapMaison"); 
		}
		else if (mapName.equals(Constantes.mapMarket)) {
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'M'));
			Fridge f = new Fridge(2, 1);
			Fridge f2 = new Fridge(3, 1);
			Fridge f3 = new Fridge(1,2);
			Fridge f4 = new Fridge(1,3);
			MarketShelve m = new MarketShelve(10,10);
			Block b = new Block(11, 10, "pancarte1");
			this.addObject(b);
			this.addObject(m);
			this.addObject(f);
			this.addObject(f2);
			this.addObject(f3);
			this.addObject(f4);
			System.out.println("Chargement MapMarket"); 
		}
		
		Window.getInstance().update();
	}
	public void initHouse(int x,int y){
		mapDrawer.removeKeyListener(Keyboard.getInstance());
		if (objectsToPlace.size() != 0 && lastObjectPlaced != objectsToPlace.get(0)) {
			initObjectInHouse(objectsToPlace.get(0), x, y);
		}
	}
	private void initObjectInHouse(GameObject o,int x,int y) {
		if (x > 0 && y >0 && x < sizeW-1 && y < sizeH-1) {
			o.setPosX(x);
			o.setPosY(y);
			addObject(o);
			if (objects.contains(o)) {
				mapDrawer.drawArrowsToDirect(o);
			}
		}
	}
	public void placeNextObject() {
		if (objectsToPlace.size()>0 && objectsToPlace.contains(lastObjectPlaced)) {
			objectsToPlace.remove(0);
	    	}
		if (objectsToPlace.size()>0) {
			mapDrawer.removeDrawArrows();
			mapDrawer.setTextToPaint("Where do you want to put the " + objectsToPlace.get(0).getClass().getSimpleName() + " ?");
		}
		else {
			mapDrawer.setTextToPaint(null);
			mapDrawer.addKeyListener(Keyboard.getInstance());
			mapDrawer.removeDrawArrows();
			mapDrawer.requestFocusInWindow();
			notInitHouse = false;
		}
		
	}
	public void addObject(GameObject o) {
		lastObjectPlaced = o;
		if (o != null) {
			if (o instanceof Sums) {
				objects.add(o);
			}
			else if (!(positionTaken[o.getPosX()][o.getPosY()])) {
		    	objects.add(o);
		    	positionTaken[o.getPosX()][o.getPosY()] = true;
		    }
			else {
				lastObjectPlaced = null;
			}
		   /* else {
		    	for (int i = 0;i< sizeW-o.getPosX(); i++) {
		    		if (!(positionTaken[i+o.getPosX()][o.getPosY()])) {
		    			o.setPosX(i+o.getPosX());
		    	    	objects.add(o);
		    	    	positionTaken[o.getPosX()][o.getPosY()] = true;
		    	    }
		    	}
		    }*/
	    }
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
	public void setObjects(ArrayList<GameObject> objectCreated) {
		if (objects != null) {
			for (GameObject o : objectCreated) {
				if (!(positionTaken[o.getPosX()][o.getPosY()])) {
			    	objects.add(o);
			    	positionTaken[o.getPosX()][o.getPosY()] = true;
				}
			    /*else {
			    	for (int i = 0;i< sizeW - o.getPosX(); i++) {
			    		if (!(positionTaken[i][o.getPosY()])) {
			    			o.setPosX(i);
			    	    	objects.add(o);
			    	    	positionTaken[i][o.getPosY()] = true;
			    	    }
			    	}
			    }*/
		    }
		}
	}
	public boolean isNotInitHouse() {
		return notInitHouse;
	}
	public void setIsInitHouse(boolean b) {
		notInitHouse = b;
	}
	public ArrayList<GameObject> getObjectsToPlace(){
		return objectsToPlace;
	}
	public GameObject getLastObjectPlace() {
		return lastObjectPlaced;
	}
	public void setLastObjectPlace(GameObject o) {
		lastObjectPlaced = o;
	}
}
