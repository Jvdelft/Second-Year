package model;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import com.fasterxml.jackson.annotation.JsonIgnore;

import controller.Keyboard;
import view.HUD;
import view.MapDrawer;
import view.Window;

public class Map implements Serializable{
	
	private static final long serialVersionUID = -2773641562525856530L;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private int sizeW;
	private int sizeH;
	private int tileSize;
	private boolean positionTaken[][];
	private String mapName;
	private transient ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private boolean notInitHouse = true;
	private ArrayList<GameObject> objectsToPlace = new ArrayList<GameObject>();
	private MapDrawer mapDrawer;
	private GameObject lastObjectPlaced = null;
	private House house = new House(20,1);
	public Map(String path) {
		if(path != Constantes.mapMaison && path != Constantes.mapMaison2) {
			notInitHouse = false;
		}
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
		if (mapName.equals(Constantes.mapBase)) {
	    	this.addObject(new Door(Math.round(sizeW/2),0, Constantes.mapRock, 'S'));
	    	this.addObject(new Door(0,Math.round(sizeH/2)-1, Constantes.mapRock, 'E'));
	    	this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapRock, 'N'));
	    	this.addObject(new Door(sizeW-1,Math.round(sizeH/2)-1, Constantes.mapRock, 'W'));
		}
		else if (mapName.equals(Constantes.mapRock)) { 
	    	this.addObject(new Door(Math.round(sizeW/2),0, Constantes.mapBase, 'S'));
	    	this.addObject(new Door(0,Math.round(sizeH/2)-1, Constantes.mapBase, 'E'));
	    	this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'N'));
	    	this.addObject(new Door(sizeW-1,Math.round(sizeH/2)-1, Constantes.mapBase, 'W'));
	    	System.out.println("Chargement map rock");

		}
		else if (mapName.equals(Constantes.mapMaison)) {
			objectsToPlace.add(new Fridge());
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			objectsToPlace.add(new Sofa(1,Math.round(sizeH/2), 0));
			objectsToPlace.add(new Toy());
			objectsToPlace.add(new Bed());
			System.out.println("Chargement MapMaison"); 
		}
		else if (mapName.equals(Constantes.mapMaison2)) {
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			this.addObject(new Door(1,0, Constantes.mapAttic, 'N'));
			objectsToPlace.add(new Sofa(1,Math.round(sizeH/2), 0));
			objectsToPlace.add(new Toy());
			objectsToPlace.add(new Bed());
			objectsToPlace.add(new Fridge());
			objectsToPlace.add(new Bed());
			System.out.println("Chargement MapMaison2"); 
		}
		else if (mapName.equals(Constantes.mapAttic)) {
			this.addObject(new Door(Math.round(sizeW/2),0, Constantes.mapMaison2, 'A'));
		}
		else if (mapName.equals(Constantes.mapMarket)) {
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'M'));
			for (int i = 4 ; i<14;  i++) {
				this.addObject(new MarketShelve(i, 2, "Apple"));
			}
			for (int i = 4 ; i<14;  i++) {
				this.addObject(new MarketShelve(i, 5, "Cigaret"));
			}
			this.addObject(new MarketShelve(4,8,"House2"));
			System.out.println("Chargement MapMarket"); 
		}
		Window.getInstance().update();
	}
	
	public void initHouse(int x,int y){
		mapDrawer = MapDrawer.getInstance();
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
	
	public void newDoor(House h, Door d) {
		ArrayList<GameObject> liste = new ArrayList<GameObject>(objects);
		for (GameObject go : liste) {
			if(go instanceof Door) {
				if ((Door)go == h.getDoor()) {objects.remove(go);}
			}
		}
		System.out.println("porte maison "+d.getPosY());
		objects.add(d);
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
			System.out.println("Bite");
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
	public void setSizeW(int i) {
		this.sizeW = i;
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
	
	public ArrayList<Sums> getSumsOnMap(){
		ArrayList<Sums> res = new ArrayList<Sums>();
		for (GameObject object : objects) {
			if (object instanceof Sums) { res.add((Sums)object);}
		}
		return res;
	}
	
	public void setObjectsOnMap(ArrayList<GameObject> objectCreated) {
		if (objects != null) {
			for (GameObject o : objectCreated) {
				if (!(positionTaken[o.getPosX()][o.getPosY()]) || o instanceof Building || o instanceof Block || o instanceof Door) {
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
		/*if (this.mapName.contentEquals("Constantes.mapBase")) {
			this.setHouse();
		}*/
	}
	public void addSumsOnMap(ArrayList<Sums> sums) {
		for (Sums s : sums) {this.addObject((GameObject) s);}
	}
	public void setHouse(House h) {
		this.house = h;
	}
	public House getHouse() { 
		return house;
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
	
	public boolean[][] getPositionTaken(){
		return positionTaken;
	}
	public void setTiles(ArrayList<BufferedImage> o) {
		this.tiles = o;
	}
	public String getMapName() {
		return mapName;
	}
}