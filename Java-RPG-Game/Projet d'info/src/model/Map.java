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
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private int sizeW;
	private int sizeH;
	private int tileSize;
	private boolean positionTaken[][];		//Les positions occupées sur la map sont représentées par un matrice de la taille de la map.
	private String mapName;
	private transient ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();	//les tiles sont les images constituant le sol de la map.
	private boolean notInitHouse = true;
	private ArrayList<GameObject> objectsToPlace = new ArrayList<GameObject>();		//Les objets à placer lors de la première entrée sur la map.
	private MapDrawer mapDrawer;
	private GameObject lastObjectPlaced = null;
	private House house = new House(20,1);
	private Dog dog;
	private transient BlockFactory factory = new BlockFactory();
	public Map(String path) {
		if(path != Constantes.mapMaison && path != Constantes.mapMaison2) {
			notInitHouse = false;
		}
		this.mapName = path;
		MapReader.readWidth(path);
		sizeW = MapReader.getwTile();
		sizeH = (sizeW * 2)/3 ;
		tileSize = 1440 / sizeW;
		positionTaken = new boolean[sizeW][sizeH];
		tiles = MapReader.ReadMap(path,this);
		initMap();
		
		
    }
	
	public void initMap() {
		if (mapName.equals(Constantes.mapBase)) {
	    	this.addObject(new Door(Math.round(sizeW/2)-1,0, Constantes.mapRock, 'S'));			//On initialise chaque map avec les portes, où elles mènent et les objets à placer.
	    	this.addObject(new Door(0,Math.round(sizeH/2)-1, Constantes.mapRock, 'E'));
	    	this.addObject(new Door(Math.round(sizeW/2)-1,sizeH-1, Constantes.mapRock, 'N'));
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
			objectsToPlace.add(factory.getInstance("Fridge", this));
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			objectsToPlace.add(factory.getInstance("Sofa", this));
			objectsToPlace.add(factory.getInstance("Toy", this));
			objectsToPlace.add(factory.getInstance("Bed", this));
			System.out.println("Chargement MapMaison"); 
		}
		else if (mapName.equals(Constantes.mapMaison2)) {
			this.addObject(new Door(Math.round(sizeW/2),sizeH-1, Constantes.mapBase, 'H'));
			this.addObject(new Door(1,0, Constantes.mapAttic, 'N'));
			objectsToPlace.add(factory.getInstance("Sofa", this));
			objectsToPlace.add(factory.getInstance("Toy", this));
			objectsToPlace.add(factory.getInstance("Bed", this));
			objectsToPlace.add(factory.getInstance("Fridge", this));
			objectsToPlace.add(factory.getInstance("Bed", this));
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
				if ((i%2) == 0) {this.addObject(new MarketShelve(i, 5, "Cigaret"));}
				else { this.addObject(new MarketShelve(i, 5, "Flower"));}
			}
			this.addObject(new MarketShelve(4,8,"House2"));
			System.out.println("Chargement MapMarket"); 
		}
		Window.getInstance().update();
	}
	
	public void initHouse(int x,int y){
		mapDrawer = MapDrawer.getInstance();
		mapDrawer.removeKeyListener(Keyboard.getInstance());							//Le personnage ne peut plus bouger et il doit placer les objets l'un à la suite des autres.
		if (objectsToPlace.size() != 0 && lastObjectPlaced != objectsToPlace.get(0)) {
			initObjectInHouse(objectsToPlace.get(0), x, y);
		}
	}
	
	private void initObjectInHouse(GameObject object,int x,int y) {			//L'objet est posé et le joueur peut l'orienter comme il le souhaite.
		GameObject o = object;
		if (x > 0 && y >0 && x < sizeW-1 && y < sizeH-1) {
			if (o.getSizeW() != 1 || o.getSizeH() != 1) {
				if (o instanceof Bed) {
					o = new Bed(x,y,this);
				}
				if (o instanceof Sofa) {
					o = new Sofa(x,y,this);
				}
				objectsToPlace.remove(object);
				objectsToPlace.add(0,o);
			}
			else{
				o.setPosX(x);
				o.setPosY(y);
			}
			addObject(o);
			if (objects.contains(o)) {
				mapDrawer.drawArrowsToDirect(o);
			}
		}
	}
	
	public void newDoor(House h, Door d) {
		ArrayList<GameObject> liste = new ArrayList<GameObject>(objects);		//Changement de porte.
		for (GameObject go : liste) {
			if(go instanceof Door) {
				if ((Door)go == h.getDoor()) {objects.remove(go);}
			}
		}
		System.out.println("porte maison "+d.getPosY());
		objects.add(d);
	}
	
	public void placeNextObject() {
		if (objectsToPlace.size()>0 && objectsToPlace.contains(lastObjectPlaced)) {		//L'objet est placé et on passe à l'objet suivant si il y en a un, on retire le texte sinon 
			objectsToPlace.remove(0);													// et le joueur peut se déplacer à nouveau.
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
	
	public void addObject(GameObject o) {			//En ajoutant l'objet, on marque la position comme prise si l'objet est un obstacle.
		if (o != null) {
			if (o instanceof Sums) {
				objects.add(o);
			}
			else if (!(positionTaken[o.getPosX()][o.getPosY()])) {
				lastObjectPlaced = o;
		    	objects.add(o);
		    	positionTaken[o.getPosX()][o.getPosY()] = true;
		    }
			else {
				lastObjectPlaced = null;
			}
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
	
	public ArrayList<ActivableObject> getActivableObjects(){				//Renvoie la liste des objets activable sur la map.
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
	
	public void setObjectsOnMap(ArrayList<GameObject> objectCreated) {		//On set les objets l'un à la suite de l'autre en marquant les positions prises.
		if (objects != null) {
			for (GameObject o : objectCreated) {
				if (o instanceof Dog) {
					dog = (Dog) o;
				}
				if (!(positionTaken[o.getPosX()][o.getPosY()]) || o instanceof Building || o instanceof Block || o instanceof Door) {
			    	objects.add(o);
			    	positionTaken[o.getPosX()][o.getPosY()] = true;
				}
		    }
		}
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
	
	public void setLastObjectPlace(Furniture o) {
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
	public Dog getDog() {
		return dog;
	}
}