package model;
import java.util.Random;
public class Toy extends Furniture{
	private int value;
	private int BringHappiness;
	public Toy(int x, int y) {
		super(x,y);
		Random r = new Random();
		value = r.nextInt(20-10+1)+1;
		BringHappiness = r.nextInt(10-5+1)+1;
		user = "Kid";
		type = "PLAY TOY";
	}
	public void activate(Sums s) {
		((Kid)s).play(this);
	}
	public int getValue(){
		return value;
	}
	public int BeHappy() {
		return BringHappiness;
	}
	public boolean isObstacle() {
		return true;
	}
	public void makeSprite() {
		sprite = Constantes.toy;
	}
	@Override
	public int getDirection() {
		return 0;
	}
}
