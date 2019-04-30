package model;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import view.Window;
import view.Map;

public class Door extends ActivableObject{
	private Map map = Map.getInstance();
	private String destination;
	private char character;

	public Door(int X, int Y, String s, char c) {
		super(X, Y);
		destination = s;
		character = c;
	}
	public Door() {
		super();
	}
	public boolean isObstacle() {
		return true;
	}
	public void activate(Sums e) {
		Game game = Game.getInstance();
		switch (destination) {
			case "MapBase" : map.changeMap(Constantes.MapBase); break;
			case "MapRock" : map.changeMap(Constantes.MapRock); break;
			case "MapMaison" : map.changeMap(Constantes.MapMaison); break;
			case "MapMarket" : map.changeMap(Constantes.MapMarket); break;
		}
		/*String path = "Constantes." + destination;
		map.changeMap(Constantes.MapRock);*/
		int sizeH = map.tileHorizontale;
		int sizeV = map.tileVerticale;
		switch (character) {
			case 'N' : e.teleportation(Math.round(sizeH/2) , 1); break;
			case 'S' : e.teleportation(Math.round(sizeH/2) ,sizeV - 2); break;
			case 'W' : e.teleportation(1, Math.round(sizeV/2) - 1); break;
			case 'E' : e.teleportation(sizeH - 2 , Math.round(sizeV/2) - 1); break;
			case 'H' : e.teleportation(e.getHouse().getDoor().getPosX(), e.getHouse().getDoor().getPosY() + 1); break;
			case 'M' : e.teleportation(6, 6); break;
		}
		game.changeMap(destination);
	}
		/*choosingMap(this.destination, e);
		if (DoorN) {
			e.teleportation(Math.round(sizeH/2)- 1 ,sizeV - 2);
			choosingMap("N",e);
		}
		else if (DoorW) {
			e.teleportation(sizeH - 2 , Math.round(sizeV/2) - 1);
			choosingMap("W",e);
		}
		else if (DoorE) {
			e.teleportation(1, Math.round(sizeV/2) - 1);
			choosingMap("E",e);
		}
		else if (DoorS) {
			if (map.map != Constantes.MapMaison) {e.teleportation(Math.round(sizeH/2) - 1, 1);}
			else {e.teleportation(e.getHouse().getDoor().getPosX(), e.getHouse().getDoor().getPosY() + 1);}
			choosingMap("S",e);
		}
		else if (this.getPosX() == e.getHouse().getDoor().getPosX() && this.getPosY() == e.getHouse().getDoor().getPosY()) {
			
			choosingMap("H",e);
		}
	}
	private void choosingMap(String s, Sums e) {
		Game game = Game.getInstance();
		if (map.map == Constantes.MapBase && s != "H") {
			map.changeMap(Constantes.MapRock);
			e.teleportation(Math.round(map.tileHorizontale/2) ,map.tileVerticale - 2);
			game.changeMap("MapTest");
		}
		else if ( map.map == Constantes.MapRock && s != "H") {
			map.changeMap(Constantes.MapBase);
			game.changeMap("MapBase");
		}
		else if (map.map == Constantes.MapMaison && s == "S") {
			map.changeMap(Constantes.MapBase);
			game.changeMap("MapBase");
		}
		else if (map.map == Constantes.MapBase && s == "H"){
			map.changeMap(Constantes.MapMaison);
			e.teleportation(Math.round(map.tileHorizontale/2) ,map.tileVerticale - 2);
			game.changeMap("MapMaison");
		}
	}*/
	public void makeSprite() {
	}
}
