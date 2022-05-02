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
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream("sums.serial"));		//on lit le game enregistré et on initialise un nouveau game
			Game game = (Game) ois.readObject();									// on donne à ce nouveau game toutes les variables stockées dans la sauvegarde.
			ois.close();
			Game newGame = Game.getInstance();
			Window.getInstance().initGame();
			newGame.setMaps(game.getMaps());
			newGame.setSums(game.getSums());
			newGame.setActivePlayer(game.getActivePlayer());
			newGame.setTime(game.getTime());
			newGame.setCurrentMap(game.getCurrentMap());
			newGame.setObjects(game.getObjects());
			newGame.setDog(game.getDog());
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
						for (int l = 0; l<((Sums) object).getObjects().size(); l++) {		// Les images ne sont pas enregistrées, il faut donc que chaque object refasse ses sprites.
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
				newGame.updateVisibleButtons();
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