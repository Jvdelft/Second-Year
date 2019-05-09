package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import view.ActionPanel;
import view.InventoryPanel;
import view.MapDrawer;
import view.Window;

public class Load {
	public static boolean load = false;
	public Load() {
		load = true;
		/*ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
			try {
				FileInputStream file = new FileInputStream("Save/Save.json");
				TotalObject DeserializedObject = mapper.readValue(file,TotalObject.class);
				Game.getInstance().getMaps().get(Constantes.mapBase).setSums(DeserializedObject.getObjects());
			}
			catch(IOException e) {
				e.printStackTrace();
			}*/
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream("sums.serial"));
			Game game = (Game) ois.readObject();
			ois.close();
			Dog.dogInstance = game.getDog();
			Game newGame = Game.getInstance();
			Window.getInstance().initGame();
			newGame.setMaps(game.getMaps());
			newGame.setSums(game.getSums());
			newGame.setActivePlayer(game.getActivePlayer());
			newGame.setTime(game.getTime());
			newGame.setCurrentMap(game.getCurrentMap());
			newGame.setObjects(game.getObjects());
			MapDrawer.getInstance().changeMap(newGame.getCurrentMap());
			newGame.changeMap(Constantes.mapBase);
			Window.getInstance().setGameObjects(newGame.getGameObjects());
			Window.getInstance().setPlayer(newGame.getActivePlayer());
			ArrayList<Map> maps = new ArrayList<Map>(newGame.getMaps().values());
			for (int i = 0; i < maps.size(); i++) {
				Map map = maps.get(i);
				for (int j = 0;j< map.getObjects().size(); j++) {
					GameObject object = map.getObjects().get(j);
					object.makeSprite();
					if (object instanceof Sums) {
						for (int l = 0; l<((Sums) object).getObjects().size(); l++) {
							((Sums) object).getObjects().get(l).makeSprite();
						}
					}
					if (object instanceof ContainerObject) {
						for (int l = 0; l<((ContainerObject) object).getObjectsContained().size(); l++) {
							((ContainerObject) object).getObjectsContained().get(l).makeSprite();
						}
					}
				}
				for (int j = 0; j<map.getObjectsToPlace().size(); j++) {
					map.getObjectsToPlace().get(j).makeSprite();
				}
				map.setTiles(MapReader.ReadMap(map.getMapName(),map));
				MapDrawer.getInstance().updateContent();
				MapDrawer.getInstance().requestFocusInWindow();
				ActionPanel.getInstance().updateActivableList();
				ActionPanel.getInstance().updateVisibleButtons();
				InventoryPanel.getInstance().updateInventory();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}