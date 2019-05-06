package model;

public class Kitchen extends ContainerObject{
	public Kitchen(int x, int y) {
		super(x,y);
		type = "COOK";
	}
	public Kitchen() {
		super();
		type = "COOK";
	}
}
