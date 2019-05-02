package model;

import view.ActionPanel;
import view.MapDrawer;
import view.Window;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.omg.CosNaming.IstringHelper;

public class Game implements DeletableObserver, Runnable {
	private HashMap<String, Map> maps= new HashMap <String, Map>();
    private ArrayList<Sums> sums = new ArrayList<Sums>();
    private Sums active_player = null;
    private Sound sound;

    private Window window;
    private int sizeW;
    private int sizeH;
    private int numberOfBreakableBlocks = 40;
    private Thread t2= new Thread(this);
    private static Game GameInstance;
    private int time;
    private Timer timer;
    private Map currentMap;
    private ArrayList<GameObject> objectsOnMap = new ArrayList<GameObject>();

    private Game(Window window) {
    	this.window = window;
        initMaps();
        sizeW = window.getMapSizeW();
        sizeH = window.getMapSizeH();
        // Creating one Player at position (1,1)
    	notifyView();
    	t2.start();
    	//sound = new Sound();
    	//sound.play("Never_Surrender");
    	givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect();
        
    }
    private void initMaps() {
    	maps.put(Constantes.mapBase, new Map(Constantes.mapBase));
    	maps.put(Constantes.mapMaison, new Map(Constantes.mapMaison));
    	maps.put(Constantes.mapMarket, new Map(Constantes.mapMarket));
    	maps.put(Constantes.mapRock, new Map(Constantes.mapRock));
    	currentMap = maps.get(Constantes.mapBase);
    	objectsOnMap = currentMap.getObjects();
    	MapDrawer.getInstance().changeMap(currentMap);
    	for(GameObject o : objectsOnMap) {
    		if (o instanceof Sums) {
    			active_player = (Sums) o;
    			changeMap(Constantes.mapBase);
    			window.setPlayer(active_player);
    			break;
    		}
    	}
    }

    public void movePlayer(int x, int y) {
    	if (active_player.isPlayable()) {
	        int nextX = active_player.getPosX() + x;
	        int nextY = active_player.getPosY() + y;
	
	        boolean obstacle = false;
	        for (GameObject object : objectsOnMap) {
	            if (object.isAtPosition(nextX, nextY)) {
	                obstacle = object.isObstacle();
	            }
	            if (obstacle == true) {
	                break;
	            }
	        }
	        active_player.rotate(x, y);
	        if (obstacle == false) {
	            active_player.move(x, y);
	            active_player.tire();
	        }
	        if (active_player instanceof Adult) {
	        	
	        }
	        ActionPanel.getInstance().updateVisibleButtons();
	        notifyView();
    	}
    }
    public void givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
            	ActionPanel.getInstance().updateActivableList();
            }
        };
        TimerTask timeTask = new TimerTask() {
        	public void run() {
        		time+=1;
        	}
        };
        TimerTask musicTask = new TimerTask() {
        	public void run() {
        		//sound.play("Never_Surrender");
        	}
        };
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(timeTask, 1000L, 1000L);
        timer.scheduleAtFixedRate(repeatedTask, 1000L, 1000L);
        //timer.scheduleAtFixedRate(musicTask, 36000L, 36000L);
    }

    public void tirePlayer() {
    	active_player.tire();
    	notifyView();
    }
    public void makeBaby(House h) {
    	System.out.println("New baby");
    	Sums k = new Kid(1,1,h);
    	objectsOnMap.add(k);
    	notifyView();
    }
    public void playerWait(long delay, Sums s, String type) {
    	setNextActivePlayer(s);
    	s.setIsPlayable(false);
    	if (type == "TOILET") {
    		Thread threadSound = new Thread (new Sound("pee",Math.round(delay)));
    		threadSound.start();
		}
    	TimerTask waitTask = new TimerTask() {
    		public void run() {
    			s.setIsPlayable(true);
    		}
    	};
    	timer.schedule(waitTask, delay);
    }
    public void setNextActivePlayer(Sums s) {
    	for (GameObject perso : objectsOnMap) {
    		if (perso instanceof Sums) {
    			if (perso != s && ((Sums) perso).isPlayable()) {
    				active_player = (Sums) perso;
    				window.setPlayer(active_player);
    				break;
    			}
    		}
    	}
    }
    public void action() {
    	ActivableObject aimedObject = null;
        Sums owner = null;
		for(GameObject object : objectsOnMap) {
			if(object.isAtPosition(active_player.getFrontX(),active_player.getFrontY())){
			    if(object instanceof ActivableObject){
			        aimedObject = (ActivableObject) object;
			    }
			}
		}
		if(aimedObject != null){
		    aimedObject.activate(active_player);
		}
        notifyView();
		}

    private void notifyView() {
        window.update();
        window.revalidate();
    }
    public static Game getInstance() {
    	if (GameInstance == null) {
    		GameInstance = new Game(Window.getInstance());
    	}
    	return GameInstance;
    }

    public ArrayList<GameObject> getGameObjects() {
        return this.objectsOnMap;
    }
    public ArrayList<ActivableObject> getActivableObjects(){
    	ArrayList<ActivableObject> list = new ArrayList<ActivableObject>();
    	for (GameObject object : objectsOnMap) {
    		if (object instanceof ActivableObject) {
    			list.add((ActivableObject) object);
    		}
    	}
    	return list;
    }
    public void setGameObject(ArrayList<GameObject> o){
    	this.objectsOnMap = o;
    }

    @Override
    synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
    	Window.getInstance().getStatus().getActionPanel().updateActivableList();
        objectsOnMap.remove(ps);
        if (loot != null) {
            objectsOnMap.addAll(loot);
        }
        notifyView();
    }


    public void playerPos() {
        System.out.println(active_player.getPosX() + ":" + active_player.getPosY());
        
    }

	public void stop() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}


	public void sendPlayer(int x, int y) {
		for (GameObject p: objectsOnMap) {
			if (p instanceof Sums && x == p.getPosX() && y == p.getPosY()) {
				active_player = (Sums) p;
				window.setPlayer((Sums)p);
			}
	}
		//Thread t = new Thread(new AStarThread(this, active_player, x,  y));
		//t.start();
	}
	public void run() {
		while (true) {
			/*for (Sums e: sums) {
				if (e instanceof Sums) {
					e.energy -=1;
					((Sums) e).faim -= 1;
					((Sums) e).toilet += 1;
					((Sums) e).hygiene -= 1;
					player_died((Sums) e);
			}
		}*/
			notifyView();
			try {
				Thread.sleep(500);
	}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
}
	public void player_died(Sums e) {
		if (e.energy == 0 || ((Sums) e).faim == 0) {
			sums.remove(e);
			objectsOnMap.remove(e);
			if (e == active_player) {
				Random r = new Random();
				int index = r.nextInt(sums.size()) + 1;
				active_player = sums.get(index); //ATTENTION REGLER OBJECTSONMAP
			}
			notifyView();
				}
			}
	public void setObjects(ArrayList<GameObject> g) {
		this.objectsOnMap = g;
		int i = 0;
		while (!(objectsOnMap.get(i) instanceof Sums)) {
			i+= 1;
		}
		active_player = (Sums)objectsOnMap.get(i);
		window.setGameObjects(g);
	}
	public void AddObject(GameObject o) {
		objectsOnMap.add(o);
	}
	public void changeMap(String s) {
		objectsOnMap.remove(active_player);
		currentMap = maps.get(s);
		MapDrawer.getInstance().changeMap(currentMap);
		objectsOnMap = currentMap.getObjects();
		sizeW = window.getMapSizeW();
        sizeH = window.getMapSizeH();
		boolean newMap = true;
  		objectsOnMap.add(active_player);
		window.setGameObjects(objectsOnMap);
		
	}
	public int getTime() {
		return time;
	}
	public HashMap<String,Map> getMaps(){
		return maps;
	}
	public Map getCurrentMap() {
		return currentMap;
	}
	public void setActivePlayer(Sums s) {
		active_player = s;
	}
}
