package model;

import view.ActionPanel;
import view.InventoryPanel;
import view.MapDrawer;
import view.Window;

import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.omg.CosNaming.IstringHelper;

import controller.Keyboard;

public class Game implements DeletableObserver, Runnable, Serializable{
	private static final long serialVersionUID = 8769447140160551649L;
	private HashMap<String, Map> maps= new HashMap <String, Map>();
    private ArrayList<Sums> sums = new ArrayList<Sums>();
    private Sums active_player = null;
    private transient Sound sound;
    private Window window;
    private int sizeW;
    private int sizeH;
    private int numberOfBreakableBlocks = 40;
    private transient Thread t2= new Thread(this);
    private static Game GameInstance;
    private int time;
    private transient ArrayList<Timer> timers = new ArrayList<Timer>();
    private transient ArrayList<Object> timerTasks = new ArrayList<Object>();
    private Map currentMap;
    private ArrayList<GameObject> objectsOnMap = new ArrayList<GameObject>();
    private int index;
    private int indexInventory;
    LocalDateTime localDateTime = LocalDateTime.of(2019, Month.JANUARY, 01, 00 , 00,00);
    private transient ArrayList<AStarThread> threads = new ArrayList<AStarThread>();
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
    	makeAllTimerTask();
    }
    private void initMaps() {
    	if (!(Load.load)) {
    		System.out.println("Hey");
    		maps.put(Constantes.mapBase, new Map(Constantes.mapBase));
    		maps.put(Constantes.mapMaison, new Map(Constantes.mapMaison));
    		maps.put(Constantes.mapMarket, new Map(Constantes.mapMarket));
    		maps.put(Constantes.mapRock, new Map(Constantes.mapRock));
    		maps.put(Constantes.mapMaison2, new Map(Constantes.mapMaison2));
    		maps.put(Constantes.mapAttic, new Map(Constantes.mapAttic));
    		for (String s: maps.keySet()) {
    			for (Sums sumsOnMap : maps.get(s).getSumsOnMap()) {
    				sums.add(sumsOnMap);
	    		}
	    	}
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
    	for(GameObject o : objectsOnMap) {
    		if (o instanceof DeletableObject) {
    			((DeletableObject) o).attachDeletable(this);
    		}
    	}
    }
    public ArrayList<Sums> getSums() {
    	return sums;
    }

    public void movePlayer(int x, int y, Sums sums) {
    	int moveX;
    	int moveY;
    	if (x>1) {
    		moveX = 1;
    	}
    	else if (x<-1) {
    		moveX = -1;
    	}
    	else {
    		moveX = x;
    	}
    	if (y>1) {
    		moveY = 1;
    	}
    	else if (y<-1) {
    		moveY = -1;
    	}
    	else {
    		moveY = y;
    	}
    	Sums p = sums;
    	if (p == null) {
    		p = active_player;
    	}
    	if (p.isPlayable()) {
	        int nextX = p.getPosX() + moveX;
	        int nextY = p.getPosY() + moveY;
	
	        boolean obstacle = false;
	        for (GameObject object : objectsOnMap) {
	            if (object.isAtPosition(nextX, nextY)) {
	                obstacle = object.isObstacle();
	            }
	            if (obstacle == true) {
	                break;
	            }
	        }
	        p.rotate(moveX, moveY);
	        if (obstacle == false) {
	            p.move(moveX, moveY);
	            if (p == active_player) {
	            	p.tire();
	            }
	        }
	        ActionPanel.getInstance().updateVisibleButtons();
	        notifyView();
    	}
    }
   
   public void buttonPressed(String button) {
	   if (button != null) {
		   if (button.contentEquals("GIVE FLOWER")) { ((Adult) getFrontObject()).receiveFlower(active_player);}
		   else if (button.contentEquals("MAKE LOVE")) { ((Adult) getFrontObject()).makeLove();}
		   else if (button.contentEquals("GO TO WORK")) { sendPlayerToWork();}
		   else if (button.contentEquals("STOCK")) { 
			   GameObject o = active_player.getObjects().get(indexInventory);
			   active_player.getObjects().remove(o);
			   ((ContainerObject) getFrontObject()).getObjectsContained().add(o);
		   }
		   else if (button.contentEquals("COOK")) {
			   GameObject object = active_player.getObjects().get(indexInventory);
	   			if (object instanceof Food) {
	   				active_player.getObjects().remove(object); 
	   				((Kitchen) getFrontObject()).getObjectsContained().add(object);
	   				if (((Kitchen) getFrontObject()).getObjectsContained().size()>1) {
	   					((Kitchen) getFrontObject()).cook(active_player);
	   				}
	   			}
		   }
		   else {
			   if (getFrontObject() != null && getFrontObject().getType().contentEquals(button)) {
				   getFrontObject().activate(active_player);
			   }
			   else {
				   ((ActivableObject) active_player.getObjects().get(indexInventory)).activate(active_player);
			   }
		   }
		   /*switch (button) {
	   		case "GIVE FLOWER" : ((Adult) getFrontObject()).receiveFlower(active_player); break;
	   		case "MAKE LOVE" : ((Adult) getFrontObject()).makeLove(); break;
	   		case "STOCK" : GameObject o = active_player.getObjects().get(indexInventory); active_player.getObjects().remove(o); ((ContainerObject) getFrontObject()).getObjectsContained().add(o);break;
	   		case "COOK" :
	   			GameObject object = active_player.getObjects().get(indexInventory);
	   			if (object instanceof Food) {
	   				active_player.getObjects().remove(object); 
	   				((Kitchen) getFrontObject()).getObjectsContained().add(object);
	   				if (((Kitchen) getFrontObject()).getObjectsContained().size()>1) {
	   					((Kitchen) getFrontObject()).cook(active_player);
	   				}
	   			}
	   			break;
	   		case "GO TO WORK" : sendPlayerToWork();
	   		default : if (getFrontObject() != null && getFrontObject().getType() == button) { getFrontObject().activate(active_player); }//action sur objet de la map
	   				  else { ((ActivableObject) active_player.getObjects().get(indexInventory)).activate(active_player); } //action sur l'inventaire
	   	   }*/
	   }
	   else {
		   if (getFrontObject() != null && getFrontObject() instanceof ActivableObject) {
			   getFrontObject().activate(active_player);
		   }
	   }
	   MapDrawer.getInstance().updateContent();
	   MapDrawer.getInstance().requestFocusInWindow();
	   ActionPanel.getInstance().updateVisibleButtons();
	   InventoryPanel.getInstance().updateInventory();
   }
   private void sendPlayerToWork() {
	   	Door closestDoor = getClosestDoor(active_player, true);
	   	Sums p = active_player;
	   	Timer timer = new Timer();
	   	TimerTask workTask = new TimerTask() {
		   public void run() {
			   	int posX = closestDoor.getPosX();
			   	int posY = closestDoor.getPosY();
       			switch(String.valueOf(closestDoor.getChar())) {
       			case "E" : posX +=1;break;
       			case "W" : posX -= 1;break;
       			case "S" : posY +=1; if (closestDoor.getPosX() == sizeW && closestDoor.getPosY() == sizeH) {posY -=1;}; break;
       			case "N" : posY += 1; break;
       			case "H" : posY-=1; break;
       			default : posY += 1; break;
       		}
			   	threads.add(0, new AStarThread(Game.getInstance(), p, posX, posY, "WORK"));
       			((AStarThread) threads.get(0)).run();
		   }
	   	};
	   	TimerTask backFromWorkTask = new TimerTask(){
	   		public void run() {
	   			p.teleportation(Math.abs(closestDoor.getPosX()-1), Math.abs(closestDoor.getPosY()-1));
	   			timers.remove(timer);
	   			((Adult) p).work();
			 	p.setIsPlayable(true);
		 }
	   };
	   timers.add(timer);
	   timer.schedule(workTask, 500);
	   timer.schedule(backFromWorkTask, 50000);
	   setNextActivePlayer(active_player);
   }
   public ActivableObject getFrontObject() {
	   ActivableObject res = null;
	   for (ActivableObject o : currentMap.getActivableObjects()) {
			if (o.getPosX() == active_player.getFrontX() && o.getPosY() == active_player.getFrontY()) {
				res = o;
			}
	   }
	   return res;
   }
   
   public void setIndexInventory(int i) {
	   if (i <0) {
		   indexInventory = 0;
	   }
	   else if (i<active_player.getObjects().size()) {
		   indexInventory = i;
	   }
	   else {
		   indexInventory = active_player.getObjects().size()-1;
	   }
   }
   
   public void makeAllTimerTask(){
       TimerTask repeatedTask = new TimerTask() {
           public void run() {
           	ActionPanel.getInstance().updateActivableList();
           }
       };
       TimerTask timeTask = new TimerTask() {
       	public void run() {
       		localDateTime = localDateTime.plusSeconds(1);
       		MapDrawer.getInstance().updateTime();
       	}
       };
       TimerTask musicTask = new TimerTask() {
       	public void run() {
       		//sound.play("Never_Surrender");
       	}
       };
       TimerTask lifeTask = new TimerTask() {
       	public void run() {
       		synchronized(sums) {
       		timePassed();
       		}
       	}
       };
       TimerTask moveTask = new TimerTask() {
       	public void run() {
       		Sums s = getRandomSums();
       		Door randomDoor = getRandomDoor();
       		Random rand = new Random();
       		int choice = rand.nextInt(2);
       		if (s != null && choice == 1) {
	        		int posX = randomDoor.getPosX();
	        		int posY = randomDoor.getPosY();
	        		switch(String.valueOf(randomDoor.getChar())) {
	        		case "E" : posX +=1;break;
	        		case "W" : posX -= 1;break;
	        		case "S" : posY +=1; if (randomDoor.getPosX() == sizeW && randomDoor.getPosY() == sizeH) {posY -=1;}; break;
	        		case "N" : posY += 1; break;
	        		case "H" : posY-=1; break;
	        		default : posY += 1; break;
	        		}
	        		threads.add(0, new AStarThread(Game.getInstance(), s, posX, posY, randomDoor));
	        		((AStarThread) threads.get(0)).run();
	        	}
       		else if (s!= null) {
       			ArrayList<Integer> list = getRandomPosition(currentMap);
       			threads.add(0, new AStarThread(Game.getInstance(), s, list.get(0), list.get(1), null));
	        		((AStarThread) threads.get(0)).run();
       		}
       	}
       };
       TimerTask comingTask = new TimerTask() {
       	public void run() {
       		ArrayList<GameObject> list = new ArrayList<GameObject>(getRandomSumsAndDoor());
       		if (list.size()>0) {
	        		Sums s = (Sums) list.get(0);
	        		Door door = (Door) list.get(1);
	        		if (s != null) {
	        			door.activate(s);
	        			ArrayList<Integer> position = getRandomPosition(maps.get(door.getDestination()));
	        			threads.add(0, new AStarThread(Game.getInstance(), s, position.get(0), position.get(1), null));
	        			((AStarThread) threads.get(0)).run();
	        		}
       		}
       	}
       };
       addList(repeatedTask, timerTasks, 1000,1000);
       addList(timeTask, timerTasks,5,5);
       addList(musicTask, timerTasks, 36000,36000);
       //addList(moveTask, timerTasks,2500,2500);
       //addList(comingTask, timerTasks,5000,5000);
       addList(lifeTask, timerTasks, 1000,1000);
       for (int i = 0; i < timerTasks.size(); i+=3) {
       	timers.add(new Timer());
       	TimerTask timerTask = (TimerTask) timerTasks.get(i);
       	long first = (long) timerTasks.get(i+1);
       	long delay = (long) timerTasks.get(i+2);
       	timers.get(i/3).scheduleAtFixedRate(timerTask, first, delay);
       }
   }
  private void addList(TimerTask timerTask, ArrayList<Object> timerTasksList, long first, long delay) {
	   timerTasksList.add(timerTask);
	   timerTasksList.add(first);
	   timerTasksList.add(delay);
  }
  private ArrayList<GameObject> getRandomSumsAndDoor(){
 		ArrayList<Map> mapList = new ArrayList<Map>(maps.values());
		Collections.shuffle(mapList);
		Sums res = null;
		Door door = null;
		ArrayList<GameObject> resList = new ArrayList<GameObject>();
		boolean isAlreadyMoving = false;
		for (Map map: mapList) {
			for (GameObject o : map.getObjects()) {
				if (o instanceof Sums && res == null && o != active_player) {
					for (AStarThread t : threads) {
						if (t.getSums() == o){
							isAlreadyMoving = true;
						}
					}
					if (!(isAlreadyMoving) && ((Sums) o).isPlayable()) {
						res = (Sums) o;
					}
				}
				if (o instanceof Door && maps.get(((Door) o).getDestination()) == currentMap) {
					door = (Door) o;
				}
				if (res != null && door != null) {
					resList.add(res);
					resList.add(door);
					break;
				}
			}
		}
		return resList;
 	}
 	private Door getRandomDoor() {
 		ArrayList<GameObject> lookingForDoors = new ArrayList<GameObject>(objectsOnMap);
 		Collections.shuffle(lookingForDoors);
 		Door door = null;
 		for (GameObject o : lookingForDoors) {
 			if (o instanceof Door) {
 				door = (Door) o;
 				break;
 			}
 		}
 		return door;
 	}
 	private ArrayList<Integer> getRandomPosition(Map map) {
 		Random rand = new Random();
 		int randX = rand.nextInt(map.getSizeW());
 		int randY = rand.nextInt(map.getSizeH());
 		ArrayList<Integer> position = new ArrayList<Integer>();
 		int y;
 		int x;
 		boolean [][] positionTaken = map.getPositionTaken();
 		if (positionTaken[randX][randY]){
 			while (positionTaken[randX][randY]) {
 				randX = rand.nextInt(map.getSizeW());
 				randY = rand.nextInt(map.getSizeH());
 			}
 		}
		position.add(randX);
		position.add(randY);
		return position;
 	}
    public void tirePlayer() {
    	active_player.tire();
    	notifyView();
    }
    private Door getClosestDoor(Sums s, boolean onlyMapDoors) {
    	Door closestDoor = null;
    	int distanceMin = 50000;
    	for (GameObject o : objectsOnMap) {
    		if (o instanceof Door && s!= null) {
    			if ((o.getPosX()-s.getPosX())*(o.getPosX()-s.getPosX()) + (o.getPosY()-s.getPosY())*(o.getPosY()-s.getPosY()) < distanceMin){
    				if (onlyMapDoors && o.getPosX() == sizeW || o .getPosX() == 0 || o.getPosX() == sizeW/2) {
    					closestDoor = (Door) o;
    					distanceMin = (o.getPosX()-s.getPosX())*(o.getPosX()-s.getPosX()) + (o.getPosY()-s.getPosY())*(o.getPosY()-s.getPosY());
    				}
    			}
    		}
    	}
    	return closestDoor;
    }
    private Sums getRandomSums() {
    	Sums res = null;
    	Collections.shuffle(objectsOnMap);
    	for (GameObject o : objectsOnMap) {
        	boolean isAlreadyMoving = false;
    		if (o instanceof Sums && o != active_player) {
    			for (AStarThread t : threads) {
    				if (t.getSums() == o) {
    					isAlreadyMoving = true;
    				}
    			}
    			if (!(isAlreadyMoving) && ((Sums) o).isPlayable()) {
        			res = (Sums) o;
        			break;
        		}
    		}
    	}
    	return res;
    }
    public void makeBaby(House h) {
    	System.out.println("New baby");
    	Sums k = new Kid(1,1,h);
    	sums.add(k);
    	objectsOnMap.add(k);
    	ActionPanel.getInstance().updateActivableList();
    	notifyView();
    }
    public void playerWait(long delay, Sums s, String type) {
    	if (s == active_player) {
    		setNextActivePlayer(s);
    	}
    	s.setIsPlayable(false);
    	int x = s.getPosX();
		int y = s.getPosY();
    	if (type == "TOILET") {
    		Thread threadSound = new Thread (new Sound("pee",Math.round(delay)));
    		threadSound.start();
		}
    	Timer timer = new Timer();
    	TimerTask waitTask = new TimerTask() {
    		public void run() {
    			s.setIsPlayable(true);
    			s.teleportation(x, y);
    			timers.remove(timer);
    		}
    	};
    	timer.schedule(waitTask, delay);
    	timers.add(timer);
    }
    
    public void setNextActivePlayer(Sums s) {
    	for (GameObject perso : objectsOnMap) {
    		if (perso instanceof Sums) {
    			if ((Sums)perso != s && ((Sums) perso).isPlayable()) {
    				active_player = (Sums)perso;
    				window.setPlayer(active_player);
    				break;
    			}
    		}
    	}
    }
    public void action() { // = appuyé sur interact ou premier bouton
    	String button = ActionPanel.getInstance().getFirstVisibleButton();
    	buttonPressed(button);
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
    	return currentMap.getActivableObjects();
    }
    public void setGameObject(ArrayList<GameObject> o){
    	this.objectsOnMap = o;
    }

    @Override
    synchronized public void delete(Deletable ps) {
        objectsOnMap.remove(ps);
        if (ps instanceof Sums) {sums.remove(ps);}
        ActionPanel.getInstance().updateActivableList();
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
				ActionPanel.getInstance().setPlayer(active_player);
				MapDrawer.getInstance().requestFocusInWindow();
			}
	}
		//Thread t = new Thread(new AStarThread(this, active_player, x,  y));
		//t.start();
	}
	public void run() {
		while (true) {
			notifyView();
			try {
				Thread.sleep(500);
	}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void timePassed() {
		ArrayList<Sums> newList = new ArrayList<Sums>(sums);
		for (Sums e: newList) {
			if (e.isPlayable()) {
				e.timePassed();
			}
		}
		notifyView();
	}
	public void sumsEvolution(Sums s, HashMap<Sums, Integer> loveHasMap, ArrayList<GameObject> inventory) {
		Sums newSums = s;
		switch (s.getAgeRange()) {
		case "Kid" : newSums = new Teen(s.getPosX(), s.getPosY(), s.getHouse()); break;
		case "Teen" : newSums = new Adult(s.getPosX(), s.getPosY(), s.getHouse()); break;
		case "Adult" : newSums = new Elder(s.getPosX(), s.getPosY(), s.getHouse()); break;
		}
		sums.remove(s); 
		newSums.setLoveHashMap(loveHasMap);
		newSums.setInventory(inventory);
		sums.add(newSums);
		s.getMap().getObjects().add(newSums);
		s.getMap().getObjects().remove(s);
		if (s == active_player) { active_player = newSums; ActionPanel.getInstance().setPlayer(active_player); window.setPlayer(active_player);}
		window.setGameObjects(objectsOnMap);
		ActionPanel.getInstance().updateActivableList();
	}
	public void playerDied(Sums e) {
		System.out.println("player died");
		sums.remove(e);
		e.getMap().getObjects().remove(e);
		if (e == active_player) {
			Random r = new Random();
			int index = r.nextInt(sums.size());
			active_player = sums.get(index); //ATTENTION REGLER OBJECTSONMAP
			if (active_player.getMap() != currentMap ) {
				changeMap(active_player.getStringMap());
			}
			ActionPanel.getInstance().setPlayer(active_player);
			window.setPlayer(active_player);
		}
		window.setGameObjects(objectsOnMap);
		ActionPanel.getInstance().updateActivableList();
	}

	public void setObjects(ArrayList<GameObject> g) {
		this.objectsOnMap = g;
		for (GameObject go : objectsOnMap) {
			if (go instanceof Sums) {sums.add((Sums)go);}
		}
		int i = 0;
		while (!(objectsOnMap.get(i) instanceof Sums)) {
			i+= 1;
		}
		active_player = (Sums)objectsOnMap.get(i);
		window.setGameObjects(g);
	}
	public ArrayList<GameObject> getObjects(){
		return this.objectsOnMap;
	}
	public void AddObject(GameObject o) {
		objectsOnMap.add(o);
	}
	public void changeMap(String s) {
		for (AStarThread  t : threads) {
			if (t != null) {
				t.stopThread();
			}
		}
		objectsOnMap.remove(active_player);
		currentMap = maps.get(s);
		MapDrawer.getInstance().changeMap(currentMap);
		objectsOnMap = currentMap.getObjects();
		sizeW = currentMap.getSizeW();
        sizeH = currentMap.getSizeH();
  		objectsOnMap.add(active_player);
  		window.setGameObjects(objectsOnMap);
  		if ((s == Constantes.mapMaison || s == Constantes.mapMaison2) && maps.get(s).isNotInitHouse()) {
  			MapDrawer.getInstance().setTextToPaint("Where do you want to put the " + currentMap.getObjectsToPlace().get(0).getClass().getSimpleName() + " ?");
  			currentMap.initHouse(0, 0);
		}
  		for(GameObject o : objectsOnMap) {
    		if (o instanceof DeletableObject) {
    			((DeletableObject) o).attachDeletable(this);
    		}
    	}
		
	}
	public LocalDateTime getTime() {
		return localDateTime;
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
	public Sums getActivePlayer() {
		return active_player;
	}
	public ArrayList<AStarThread> getThreads() {
		return threads;
	}
	public void setMaps(HashMap<String,Map> hm) {
		this.maps = hm;
	}
	public void setGameInstance(Game gI) {
		synchronized(this) {
			this.GameInstance = gI;
		}
	}
	public void setCurrentMap(Map m) {
		this.currentMap = m;
	}
	public void setSums(ArrayList<Sums> s) {
		this.sums = s;
	}
	public void setTime(LocalDateTime time2) {
		this.localDateTime = time2; 
		
	}
}