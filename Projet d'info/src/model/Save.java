package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Save {
	public Save() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter();
		Map map = Game.getInstance().getMaps().get(Constantes.mapBase);
		TotalObject SerializedObject = new TotalObject();
		ArrayList<Sums> objectsToSerialize = new ArrayList<Sums>();
		for (int i = 0; i<map.getObjects().size(); i++) {
			if (map.getObjects().get(i) instanceof Sums) {
				objectsToSerialize.add((Sums) map.getObjects().get(i));
			}
		}
			/*try {
				File file = new File("Save/Save.json");
				mapper.writeValue(file, SerializedObject);
			}
			catch(IOException e) {
				e.printStackTrace();
			}*/
		Game game = Game.getInstance();
		SerializedObject.setObjects(game.getMaps());
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("sums.serial"));
			oos.writeObject(game);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Writes object to the file oos.flush(); // Flushes the buffer 
		}
	}

