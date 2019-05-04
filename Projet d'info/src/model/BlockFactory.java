package model;

public class BlockFactory {
	public GameObject getInstance(String type,int posX,int posY) {
		GameObject res = null;
		switch (type){
			case "Border": res = new Border(posX,posY); break;
		}
		return res;
	}

}