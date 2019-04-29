package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import view.Map;

public class Save {
	private Map map = Map.getInstance();
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	public Save() {
		objects = map.getObjects();
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("Save/Save.json");
		mapper.enableDefaultTyping();
		TotalObject SerializedObject = new TotalObject();
		SerializedObject.setObjects(objects);
			try {
				mapper.writeValue(file, SerializedObject);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

