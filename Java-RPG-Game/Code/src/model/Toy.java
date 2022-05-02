package model;
import java.util.Random;
public class Toy extends Furniture{
	private int value;
	private int BringHappiness;
	public Toy(int x, int y) {
		super(x,y);
		setValues();
	}
	public Toy() {
		super();
		setValues();
	}
	private void setValues() {
		Random r = new Random();
		value = r.nextInt(20-10+1)+1;
		BringHappiness = r.nextInt(10-5+1)+1;
		user = "Kid";
		type = "PLAY TOY";
	}
	public int getValue(){
		return value;
	}
	public int getHappiness() {
		return BringHappiness;
	}
	@Override
	public boolean isObstacle() {
		return true;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.toy;
	}
	@Override
	public int getDirection() {
		return 0;
	}
	@Override
	public void activate(Sums s) {
		if (s instanceof Kid) {
			((Kid)s).play(this);
		}
	}
}
