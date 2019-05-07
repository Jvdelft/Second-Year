package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TotalObject implements Serializable{
	private HashMap<String, Map> objects;
	public TotalObject() {
		
	}
	public HashMap<String, Map> getObjects() {
		return objects;
	}
	public void setObjects(HashMap<String, Map> o) {
		this.objects = o;
	}

}
