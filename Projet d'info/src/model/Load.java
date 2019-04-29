package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Load {
	public Load() {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("Save/Save.json");
		mapper.enableDefaultTyping();
			try {
				TotalObject DeserializedObject = mapper.readValue(file,TotalObject.class);
				ArrayList<GameObject> loadedObjects = DeserializedObject.getObjects();
				Game.getInstance().setObjects(loadedObjects);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}