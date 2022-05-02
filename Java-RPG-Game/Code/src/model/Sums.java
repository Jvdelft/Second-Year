package model;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import view.InventoryPanel;
import view.Window;

public abstract class Sums extends ActivableObject implements NeedToEat, Directable, DeletableObserver{
	protected double age;
	protected int faim;
	protected int toilet;
	protected int hygiene;
	protected int max_faim;
	protected int max_energy;
	protected int max_toilet;
	protected int cost;
	protected int happiness;
	protected int max_happiness;
	protected House maison;
	protected int energy;
	protected int direction = EAST ;
	protected String ageRange ;
	protected String typeAffection = type;
	protected int max_hygiene = Constantes.max_hygiene;
	private HashMap<Sums, Integer> loveHashMap = new HashMap <Sums, Integer>();		//Dictionnaire contenant tous les sums et l'affection que ce sums leur porte sous la forme d'un entier.
	protected transient BufferedImage sprite_l;
	protected transient BufferedImage sprite_r;
	protected transient BufferedImage sprite_u;
	protected transient BufferedImage sprite_d;
	protected transient BufferedImage sprite_face;
	private ArrayList<GameObject> inventory = new ArrayList<GameObject>();
	protected boolean playable = true;
	public Sums(int x, int y, House h) {
		super(x, y);
		this.faim = max_faim;
		this.energy = max_energy;
		this.happiness = max_happiness;			//On créer le Sums en initialisant ses besoins au maximum.
		this.hygiene = max_hygiene;
		this.toilet = 10;
		h.AddHabitant(this);
		this.maison = h;
		inventory.add(new Apple());
		inventory.add(new Apple());
		inventory.add(new Apple());
		inventory.add(new Apple());
		for (GameObject go : inventory) {
			if (go instanceof DeletableObject) {((DeletableObject)go).attachDeletable(this); }
		}
	}
	public void Eat(Food f) {
		int hungry = this.faim + f.getNutritionalValue();
		this.faim = Math.min(hungry,this.max_faim);
		int energie = this.energy + f.getEnergyValue();
		this.energy = Math.min(energie, this.max_energy);
	}
	public void move(int X, int Y) {
        this.posX = this.posX + X;
        this.posY = this.posY + Y;
    }
	public void sleep() {
		int energie = this.energy + 20;
		this.energy = Math.min(energie, this.max_energy);
	}
	public void timePassed() {			//les besoins évoluent avec le temps et les Sums évoluent si besoin.
		if (hygiene < 0) {
			energy -= 1;
		}
		if(toilet>this.getMaxToilet()) {
			energy -=1;
		}
		energy -=1;
		faim -= 1;
		toilet += 1;
		hygiene -= 2;
		age += 1;
		if (energy == 0 || faim == 0) {
			Game.getInstance().playerDied(this);
		}
		if (ageRange == "Kid" && age >= 12) {
			Game.getInstance().sumsEvolution(this, this.loveHashMap);
		}
		else if (ageRange == "Teen" && age >= 21) {
			Game.getInstance().sumsEvolution(this, this.loveHashMap);
		}
		else if (ageRange == "Adult" && age >= 60) {
			Game.getInstance().sumsEvolution(this, this.loveHashMap);
		}
		else if (ageRange == "Elder" && age >= 85) {
			Game.getInstance().playerDied(this);
		}
	}
	public void buy(GameObject object) {
		this.getHouse().changeMoney(-(object.getPrice()) );
		if (object instanceof ImageDraw) {
			this.getHouse().setCategory(2);
		}
		else{
			addInInventory(object);
		}
	}

    public void rotate(int x, int y) {
        if(x == 0 && y == -1)
            direction = NORTH;
        else if(x == 0 && y == 1)
            direction = SOUTH;
        else if(x == 1 && y == 0)
            direction = EAST;
        else if(x == -1 && y == 0)
            direction = WEST;
    }
    public void interraction(Sums s, int valeur) {    //Interaction entre les Sums.
    	boolean newFriend = true;
    	for (Sums key : loveHashMap.keySet()) {
			if (key.equals(s)) {
				newFriend = false;
			}
		}
		if (newFriend) {
			loveHashMap.put(s, new Integer(0));
		}
		Integer affection = loveHashMap.get(s);
		affection = affection + valeur;
		loveHashMap.put(s, affection);
    }
    public void tire() {
		if (energy > 20)
			energy -= 1;
	}
    public Map getMap() {
		HashMap<String, Map> maps = Game.getInstance().getMaps();	//Donne la map sur laquelle est le Sums.
		for (String s: maps.keySet()) {
			for (Sums sumsOnMap : maps.get(s).getSumsOnMap()) {
				if (sumsOnMap == this) { return maps.get(s); }
			}
		}
		return maps.get(Constantes.mapBase);
	}
	public String getStringMap() {				//Donne la key de cette même map.
		String res = "";
		HashMap<String, Map> maps = Game.getInstance().getMaps();
		for (String s: maps.keySet()) {
			for (Sums sumsOnMap : maps.get(s).getSumsOnMap()) {
				if (sumsOnMap == this) { return s; }
			}
		}
		return res;
	}
	public void setLoveHashMap (HashMap<Sums, Integer> hashmap) {
		loveHashMap = hashmap;
	}
    
    public int getAffection(Sums s) {
    	int res = 0;
    	for (Sums key : loveHashMap.keySet()) {
			if (key.equals(s)) {
				res = loveHashMap.get(s);
			}
    	}
    	return res;
    }
    public String getTypeAffection() {
    	return typeAffection;
    }
    
    public void addInInventory (GameObject object) {
    	inventory.add(object);
    	if (object instanceof DeletableObject) {
    		((DeletableObject) object).attachDeletable(this);
    	}
    	InventoryPanel.getInstance().updateInventory();
    }
    
    public void setInventory(ArrayList<GameObject> go) {
    	inventory.clear();
    	inventory = go;
    	InventoryPanel.getInstance().updateInventory();
    }

    public int getFrontX() {
        int delta = 0;
        if (direction % 2 == 0){
            delta += 1 - direction;
        }
        return this.posX + delta;
    }
    public int getFrontY() {
        int delta = 0;
        if (direction % 2 != 0){
            delta += direction - 2;
        }
        return this.posY + delta;
    }
    
    public String getAgeRange() {
    	return ageRange;
    }
	public int getToilet() {
		return this.toilet;
	}
	public void setToilet(int t) {
		int toi = Math.min(t,this.max_toilet);
		this.toilet = toi;
	}
    public int getFaim() {
    	return faim;
    }
    public void setFaim(int f) {
    	int fa = Math.min(f, this.max_faim);
    	this.faim = fa;
    }
    public int getHygiene() {
    	return hygiene;
    }
    public void setHygiene(int h) {
    	int hyg = Math.min(h, this.max_hygiene);
    	this.hygiene = hyg;
    }
    public int getHappiness() {
    	return happiness;
    }
    public void setHappiness(int h) {
    	int hap = Math.min(h, this.max_happiness);
    	this.happiness = hap;
    }
    public House getHouse() {
    	return maison;
    }
    public void setHouse(House h) {
    	this.maison = h;
    }
    public ArrayList<GameObject> getObjects(){
    	return this.inventory;
    }
    public boolean isPlayable() {
    	return playable;
    }
    public void setIsPlayable(boolean ip) {
    	this.playable = ip;
    }
    public void setDirection(int direction) {
    	this.direction = direction;
    }
    public void setEnergy(int e) {
    	int ene = Math.min(e, this.max_energy);
    	this.energy = ene;
    }
    public double getEnergy() {
    	return energy;
    }
    public int getMaxEnergy() {
    	return this.max_energy;
    }
    public int getMaxToilet() {
    	return this.max_toilet;
    }
    public int getMaxFaim() {
    	return this.max_faim;
    }
    public int getMaxHygiene() {
    	return this.max_hygiene;
    }
    public int getMaxHappiness() {
    	return this.max_happiness;
    }
    public ArrayList<GameObject> getInventory(){
    	return inventory;
    }
    public BufferedImage getFaceSprite() {
    	return this.sprite_face;
    }
    @Override
    public void pee() {
    	Game.getInstance().playerWait(10000L, this, this.ageRange);
    }
    @Override
	public void activate (Sums s) {}
    @Override
    public BufferedImage getSprite() {
    	BufferedImage sprite = null;
    	switch(this.getDirection()) {
    	case (EAST) : sprite = this.sprite_r; break;
    	case(WEST) : sprite = this.sprite_l; break;
    	case(NORTH) : sprite = this.sprite_u; break;
    	case(SOUTH) : sprite = this.sprite_d; break;
    	}
    	return sprite;
    }
    @Override
    public boolean isObstacle() {
        return false;
    }

    @Override
    public int getDirection() {
    return direction;
    }
    @Override
    public void delete(Deletable d) {
        inventory.remove(d);
        InventoryPanel.getInstance().updateInventory();
    }
}