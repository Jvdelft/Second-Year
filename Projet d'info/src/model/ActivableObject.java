package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ActivableObject extends GameObject implements Activable {
	protected int lifePoints;
	@JsonIgnore
	protected String type = "INTERACT";
	@JsonIgnore
	protected String user = "All";
	
	public ActivableObject(int x, int y) {
		super(x,y);
	}
	public ActivableObject() {
		super();
	}
	public abstract void activate(Sums s) ;

	public String getType() {
		return type;
	}
	public String getUser() { 
		return user;
	}

}
