package model;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import view.Window;
import view.Map;

public class Door extends ActivableObject{
	private Map map = Map.getInstance();

	public Door(int X, int Y) {
		super(X, Y);
	}
	public Door() {
		super();
	}
	public boolean isObstacle() {
		return true;
	}
	public void activate(Sums e) {
		int sizeH = map.tileHorizontale;
		int sizeV = map.tileVerticale;
		boolean DoorN = this.getPosY() == 0;
		boolean DoorW = this.getPosX() == 0;
		boolean DoorE = this.getPosX() == 29;
		boolean DoorS = this.getPosY()== 19;
		if (DoorN) {
			e.teleportation(Math.round(sizeH/2)- 1 ,sizeV - 2);
			choosingMap("N");
		}
		else if (DoorW) {
			e.teleportation(sizeH - 2 , Math.round(sizeV/2) - 1);
			choosingMap("W");
		}
		else if (DoorE) {
			e.teleportation(1, Math.round(sizeV/2) - 1);
			choosingMap("E");
		}
		else if (DoorS) {
			e.teleportation(Math.round(sizeH/2) - 1, 1);
			choosingMap("S");
		}
		else if (this.getPosX() == e.getHouse().getDoor().getPosX() && this.getPosY() == e.getHouse().getDoor().getPosY()) {
			choosingMap("H");
		}
	}
	private void choosingMap(String s) {
		if (map.map == Constantes.MapBase && s != "H") {
			map.changeMap(Constantes.MapRock);
			
		}
		else if ( map.map == Constantes.MapRock && s != "H") {
			map.changeMap(Constantes.MapBase);
		}
		else {
			map.changeMap(Constantes.MapMaison);
		}
	}
	public void makeSprite() {
	}
}
