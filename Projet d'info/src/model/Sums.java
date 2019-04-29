package model;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Sums extends GameObject implements NeedToEat, Directable{
	protected String gender;
	protected double age;
	public int faim;
	public int toilet;
	public int hygiene;
	protected int max_faim;
	protected int max_energy;
	protected int max_toilet;
	protected int cost;
	public int happiness;
	protected int max_happiness;
	protected House maison;
	public int energy;
	private int direction = EAST ;
	@JsonIgnore
	public BufferedImage Sprite_l;
	@JsonIgnore
	public BufferedImage Sprite_r;
	@JsonIgnore
	public BufferedImage Sprite_u;
	@JsonIgnore
	public BufferedImage Sprite_d;
	@JsonIgnore
	public BufferedImage Sprite_face;
	private ArrayList<GameObject> inventory = new ArrayList<GameObject>();
	public Sums(int x, int y, House h) {
		super(x, y);
		this.faim = max_faim;
		this.energy = max_energy;
		this.happiness = max_happiness;
		h.AddHabitant(this);
		maison = h;
		inventory.add(new Apple());
		inventory.add(new Apple());
		inventory.add(new cigaret());
		inventory.add(new Apple());
		inventory.add(new Apple());
	}
	public Sums() {
		super();
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

   // //////////////////////////////////////////////////////////////////////////////////////


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
    
    public double getEnergy() {
    	return energy;
    }

	public void tire() {
		if (energy > 10)
			energy -= 10;
	}
	public void Toilet() {
		this.toilet = 0;
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

}
