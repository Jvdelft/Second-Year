package model;

public class Bath extends ActivableObject{
	private Map map;
	public Bath(int x, int y, Map map) {
		super(x,y);
		type = "BATH";
		this.map = map;
		sizeH = 2;
		sizeW = 1;
		for (int i = 0; i < sizeH-1 ; i++) {
			Bath bath = new Bath();
			bath.setPosX(x);
			bath.setPosY(y+i+1);
			map.addObject(bath);
		}
	}
	public Bath() {
		super();
		type = "BATH";
		sprite = null;  //il est important de mettre le sprite à la valeur null car comme le sprite du bain s'étend sur 2 cases, le sprite est dessiné une fois mais les deux cases
	}					//vont être activable.
	@Override
	public void activate(Sums s) {			//Les bains amènent propreté et bonheur :).
		s.hygiene = s.getMaxHygiene();
		s.setHappiness(s.getHappiness()+10);   //on passe par des getters et des setters pour s'assurer que les valeurs max ne sont pas dépassées.
		Game.getInstance().playerWait(10000, s, null);
	}
	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void makeSprite() {
		sprite = Constantes.bath;
	}
}
