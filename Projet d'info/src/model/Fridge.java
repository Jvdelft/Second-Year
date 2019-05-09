package model;

public class Fridge extends ContainerObject implements Activable{
	public Fridge(int x, int y) {
		super(x,y);
	}
	public Fridge() {	//Le constructeur vide est nécessaire pour que le joueur puisse choisir où il mets le Frigo et donc qu'on définisse la position après la création de l'objet.
		super();
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.fridge;
	}

}
