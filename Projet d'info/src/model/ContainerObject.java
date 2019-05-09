package model;

import java.util.ArrayList;

import controller.Keyboard;
import view.MapDrawer;

public class ContainerObject extends Furniture{
	protected ArrayList<GameObject> objectContained = new ArrayList<GameObject>();
	private int row;
	public ContainerObject(int x, int y) {
		super(x,y);
		type = "OPEN";
	}
	public ContainerObject() {
		super();
		type = "OPEN";
	}
	public void activate(Sums s) {
		this.open();
		s.setIsPlayable(false);
	}
	public void open(ContainerObject this) {
		this.type = "STOCK";
		MapDrawer.getInstance().drawContent(this);
	}
	public ArrayList<GameObject> switchRow(int row) {
		this.row = row;
		ArrayList<GameObject> objectsToReturn = new ArrayList<GameObject>(4);
		for (int i = 0; i < 4; i++) {
			if (i+4*row < objectContained.size()) {
				objectsToReturn.add(objectContained.get(i+row*4));
			}
		}
		return objectsToReturn;
	}
	public void close() {
		this.type = "OPEN";
	}
	public ArrayList<GameObject> getObjectsContained() {
		return objectContained;
	}
	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		
	}
	@Override
	public int getDirection() {
		return direction;
	}

}
