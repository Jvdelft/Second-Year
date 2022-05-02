package model;

public abstract class ActivableObject extends GameObject implements Activable {
	protected String type = "INTERACT"; //on d�finit un type afin que le programme sache quel JButton afficher.
	protected String user = "All";      //on d�finit les utilisateurs possibles afin que seuls certains personnages puissent utiliser l'objet.
	protected boolean other = false;    //on d�finit un bool�en qui d�termine si l'utilisation de l'objet se fait sur le personnage jou� ou un autre.
	public ActivableObject(int x, int y) {
		super(x,y);
	}
	public ActivableObject() {
		super();
	}
	public boolean isForAnotherSums() {
		return other;
	}
	public abstract void activate(Sums s) ;

	public String getType() {
		return type;
	}
	public String getUser() { 
		return user;
	}

}
