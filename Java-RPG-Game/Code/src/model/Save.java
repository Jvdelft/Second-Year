package model;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Save {
	public Save() {
		Game game = Game.getInstance();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("sums.serial"));		//On sauvegarde le game en cours dans un fichier serial.
			oos.writeObject(game);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}
	}

