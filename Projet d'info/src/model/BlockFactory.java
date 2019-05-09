package model;

public class BlockFactory {
	public GameObject getInstance(String type,int posX,int posY, Map map) {   //La factory permet de construire des objects en dehors des classes pour plus de simplicité.
		GameObject res = null;
		switch (type){
			case "Border": res = new Block(posX,posY); break;
			case "Cigaret" : res = new Cigaret(posX, posY); break;
			case "Apple" : res = new Apple(posX,posY); break;
			case "Adult" : res = new Adult(posX, posY, map.getHouse()); break;
			case "Kid" : res = new Kid(posX, posY, map.getHouse()); break;
			case "Elder" : res = new Elder(posX, posY, map.getHouse()); break;
			case "Teen" : res = new Teen(posX, posY, map.getHouse()); break;
			case "Toilet" : res = new Toilet(posX, posY); break;
			case "Kitchen" : res = new Kitchen(posX, posY, map); break;
			case "carpet" : res = new ImageDraw(posX,posY,"carpet"); break;
			case "stairsDownPart" : res = new ImageDraw(posX,posY,"stairsDownPart"); break;
			case "stairsMiddlePart" : res = new ImageDraw(posX,posY,"stairsMiddlePart"); break;
			case "stairsUpPart" : res = new ImageDraw(posX,posY,"stairsUpPart"); break;
			case "table" : res = new Block(posX,posY,"table"); break;
			case "pancarte1" : res = new Block(posX, posY, "pancarte1"); break;
			case "pancarte10" : res = new Block(posX,posY, "pancarte10"); break;
			case "pancarte50" : res = new Block(posX,posY,"pancarte50"); break;
			case "Bath" : res = new Bath(posX,posY,map); break;
			case "Dog" : res = new Dog(posX,posY); break;
		}
		return res;
	}

}