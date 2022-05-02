package model;

public class Fridge extends ContainerObject {
	public Fridge(int x, int y) {
		super(x,y);
	}
	public Fridge() {	//Le constructeur vide est n�cessaire pour que le joueur puisse choisir o� il mets le Frigo et donc qu'on d�finisse la position apr�s la cr�ation de l'objet.
		super();
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.fridge;
	}

}
