package model;

public abstract class Building extends GameObject{ //Il s'agit d'une superClasse contenant tous les batiments.
	public Building(int x, int y) {
		super(x,y);
	}
	protected Building() { super();}
	public abstract Door getDoor();
	

}
