package model;

import view.Map;
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
	private HashMap<String, ArrayList<GameObject>> objectDictionary= new HashMap <String, ArrayList<GameObject>>();
    private ArrayList<Sums> sums = new ArrayList<Sums>();
    private ArrayList<GameObject> objectsOnMap = new ArrayList<GameObject>();
    private Sums active_player = null;
    private ArrayList<GameObject> initialisation = new ArrayList <GameObject>();
    private Sound sound;

    private Window window;
    private int sizeW;
    private int sizeH;
    private int numberOfBreakableBlocks = 40;
    private Thread t2= new Thread(this);
    private static Game GameInstance;
    private int time;
    private Timer timer;

    private Game(Window window) {
    	this.window = window;
        sizeW = window.getMapSizeW();
        sizeH = window.getMapSizeH();
        // Creating one Player at position (1,1)
        if (objectDictionary.isEmpty()) {
        	initialisation = mapConstructor("MapBase");
			objectDictionary.put("MapBase", initialisation);
        	changeMap("MapBase");
        }
    	notifyView();
    	t2.start();
    	//sound = new Sound();
    	//sound.play("Never_Surrender");
    	givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect();
        
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
	        Window.getInstance().getStatus().getActionPanel().updateVisibleButtons();
	        notifyView();
    	}
    }
    public void givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
            	Window.getInstance().getStatus().getActionPanel().updateActivableList();
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
		sizeW = window.getMapSizeW();
        sizeH = window.getMapSizeH();
		boolean newMap = true;
		objectsOnMap.remove(active_player);
		for (String key : objectDictionary.keySet()) {
			if (key.equals(s)) {
				newMap = false;
			}
		}
		if (newMap) {
			initialisation = new ArrayList<GameObject>();
			initialisation = mapConstructor(s);
			objectDictionary.put(s, initialisation);
		}
		objectsOnMap = objectDictionary.get(s);
		objectsOnMap.add(active_player);
		window.setGameObjects(objectsOnMap);
		
	}
	
	private ArrayList<GameObject> mapConstructor(String map){
		House h = new House(21,2);
		for (int i = 0; i < sizeW; i++) {
    		initialisation.add(new Border(i, 0));
    		initialisation.add(new Border(i, sizeH - 1));
    		if (i >= sizeW - sizeH) {
    			initialisation.add(new Border(0, i-(sizeW-sizeH)));
    			initialisation.add(new Border(sizeW - 1, i-(sizeW-sizeH)));
    		}
		}
		if (map.equals("MapBase")) {
	    	Sums p = new Adult(10, 10,h);
	    	Sums q = new Kid(5,5,h);
	    	Sums r = new Elder(15,15,h);
	    	Sums s = new Teen(24,5,h);
	    	Market m = new Market(4,2);
	    	sums.add(p);
	    	sums.add(q);
	    	sums.add(r);
	    	sums.add(s);
	    	
	    	initialisation.add(h);
	    	initialisation.add(p);
	    	initialisation.add(q);
	    	initialisation.add(r);
	    	initialisation.add(s);
	    	initialisation.add(m);
	    	ArrayList <Building> building = new ArrayList<Building>();
	    	building.add(h);
	    	building.add(m);
	    	active_player = p;
	    	window.setPlayer(active_player);
			for (int i = 0; i < sizeW; i++) {
	    		initialisation.add(new Border(i, 0));
	    		initialisation.add(new Border(i, sizeH - 1));
	    		if (i>9) {
	    			initialisation.add(new Border(0, i-10));
	    			initialisation.add(new Border(sizeW - 1, i-10));
	    		}
	    	}
			for (Building b : building) {
				for (int i = 0; i< b.getSizeH(); i++) {
					initialisation.add(new Border(b.getPosX(),i+b.getPosY()));
					initialisation.add(new Border (b.getPosX()+b.getSizeW()-1,i+b.getPosY()));
				}
				for (int i = 0; i< b.getSizeH(); i++) {
					initialisation.add(new Border(i+b.getPosX(),b.getPosY()));
					initialisation.add(new Border (i+b.getPosX(),b.getPosY()+b.getSizeH()-1));
				}
				initialisation.add(b.getDoor());
			}
			
	    	Random rand = new Random();
	    	for (int i = 0; i < numberOfBreakableBlocks/2; i++) {
	    		int x = rand.nextInt(sizeW-4) + 2;
	    		int y = rand.nextInt(sizeH-4) + 2;
	    		Food test = new Apple(x,y);
	    		test.attachDeletable(this);
	    		initialisation.add(test);
	    	}
	    	initialisation.add(new Door(Math.round(sizeW/2),0, "MapRock", 'S'));
	    	initialisation.add(new Door(0,Math.round(sizeH/2)-1, "MapRock", 'E'));
	    	initialisation.add(new Door(Math.round(sizeW/2),sizeH-1, "MapRock", 'N'));
	    	initialisation.add(new Door(sizeW-1,Math.round(sizeH/2)-1, "MapRock", 'W'));

		}
		else if (map.equals("MapRock")) { //modifier sizeW, sizeH en fonction de la taille de la map
	    	Random rand = new Random();
	    	for (int i = 0; i < numberOfBreakableBlocks/5; i++) {
	    		int x = rand.nextInt(sizeW-4) + 2;
	    		int y = rand.nextInt(sizeH-4) + 2;
	    		Food test = new Apple(x,y);
	    		test.attachDeletable(this);
	    		initialisation.add(test);
	    	}
	    	initialisation.add(new Door(Math.round(sizeW/2),0, "MapBase", 'S'));
	    	initialisation.add(new Door(0,Math.round(sizeH/2)-1, "MapBase", 'E'));
	    	initialisation.add(new Door(Math.round(sizeW/2),sizeH-1, "MapBase", 'N'));
	    	initialisation.add(new Door(sizeW-1,Math.round(sizeH/2)-1, "MapBase", 'W'));
	    	System.out.println("Chargement map rock");

		}
		else if (map.equals("MapMaison")) {
			initialisation.add(new Adult(10,4,h));
			initialisation.add(new Fridge(10,1));
			initialisation.add(new Door(Math.round(sizeW/2),sizeH-1, "MapBase", 'H'));
			initialisation.add(new Toilet(Math.round(sizeW/2), 1));
			System.out.println("Chargement MapMaison"); 
		}
		else if (map.equals("MapMarket")) {
			initialisation.add(new Door(Math.round(sizeW/2),sizeH-1, "MapBase", 'M'));
			Fridge f = new Fridge(2, 1);
			Fridge f2 = new Fridge(3, 1);
			Fridge f3 = new Fridge(1,2);
			Fridge f4 = new Fridge(1,3);
			initialisation.add(f);
			initialisation.add(f2);
			initialisation.add(f3);
			initialisation.add(f4);
			System.out.println("Chargement MapMarket"); 
		}
		return initialisation;
	}
}
