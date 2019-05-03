package model;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import view.Window;

public abstract class Sums extends ActivableObject implements NeedToEat, Directable{
	protected String gender;
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
	private HashMap<Sums, Integer> loveHashMap = new HashMap <Sums, Integer>();
	@JsonIgnore
	protected BufferedImage sprite_l;
	@JsonIgnore
	protected BufferedImage sprite_r;
	@JsonIgnore
	protected BufferedImage sprite_u;
	@JsonIgnore
	protected BufferedImage sprite_d;
	@JsonIgnore
	protected BufferedImage sprite_face;
	private ArrayList<GameObject> inventory = new ArrayList<GameObject>();
	protected boolean playable = true;
	public Sums(int x, int y, House h) {
		super(x, y);
		if (this.energy == 0) {
			this.faim = max_faim;
			this.energy = max_energy;
			this.happiness = max_happiness;
			this.toilet = 10;
		}
		h.AddHabitant(this);
		this.maison = h;
		inventory.add(new Apple());
		inventory.add(new Apple());
		inventory.add(new Cigaret());
		inventory.add(new Apple());
		inventory.add(new Apple());
	}
	public Sums() {
		super();
		this.energy = 1;
	}
	
	public void Eat(Food f) {
		int hungry = this.faim + f.getNutritionalValue();
		this.faim = Math.min(hungry,this.max_faim);
		int energie = this.energy + f.getEnergyValue();
		this.energy = Math.min(energie, this.max_energy);
	}
	public void Drink(Drinks d) {
		int energie = this.energy + d.getEnergyValue();
		this.energy = Math.min(energie, this.max_energy);
		int Vessie = this.toilet + d.MakeUPee();
		this.toilet = Math.min(this.max_toilet, Vessie);
	}
	public void move(int X, int Y) {
        this.posX = this.posX + X;
        this.posY = this.posY + Y;
    }
	public void sleep() {
		int energie = this.energy + 20;
		this.energy = Math.min(energie, this.max_energy);
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
    
    public void pee() {
    	Game.getInstance().playerWait(10000L, this, this.ageRange);
    }
    
    public void activate(Sums s) {

    }
    public void interraction(Sums s, int valeur) {
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
   // //////////////////////////////////////////////////////////////////////////////////////
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

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public int getDirection() {
    return direction;
    }
    @JsonIgnore
    public int getFrontX() {
        int delta = 0;
        if (direction % 2 == 0){
            delta += 1 - direction;
        }
        return this.posX + delta;
    }
    @JsonIgnore
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
    
    public double getEnergy() {
    	return energy;
    }

	public void tire() {
		if (energy > 20)
			energy -= 1;
	}
	public int getToilet() {
		return this.toilet;
	}
	public void setToilet(int t) {
		this.toilet = t;
	}
    public int getFaim() {
    	return faim;
    }
    public void setFaim(int f) {
    	this.faim = f;
    }
    public int getHygiene() {
    	return hygiene;
    }
    public void setHygiene(int h) {
    	this.hygiene = h;
    }
    public int getHappiness() {
    	return happiness;
    }
    public void setHappiness(int h) {
    	this.happiness = h;
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
    public BufferedImage getFaceSprite() {
    	return this.sprite_face;
    }
    public void setDirection(int direction) {
    	this.direction = direction;
    	makeSprite();
    }
    public void setEnergy(int e) {
    	this.energy = e;
    }


}
