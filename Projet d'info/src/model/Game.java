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
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.omg.CosNaming.IstringHelper;

import controller.Keyboard;

public class Game implements DeletableObserver, Serializable{
	private HashMap<String, Map> maps= new HashMap <String, Map>();     //Les maps sont stockées dans des hashmaps dont les clés sont les chemins 
    private ArrayList<Sums> sums = new ArrayList<Sums>();				//vers les fichiers texte contenant les tiles des maps.
    private Sums active_player = null;
    private Window window;
    private int sizeW;
    private int sizeH;
    private static Game GameInstance;
    private transient ArrayList<Timer> timers = new ArrayList<Timer>();
    private transient ArrayList<Object> timerTasks = new ArrayList<Object>();
    private Map currentMap;
    private ArrayList<GameObject> objectsOnMap = new ArrayList<GameObject>();
    private int indexInventory;
    LocalDateTime localDateTime = LocalDateTime.of(2019, Month.JANUARY, 01, 00 , 00,00);
    private transient ArrayList<AStarThread> threads = new ArrayList<AStarThread>();
    private Dog dog;
    private Game(Window window) {
    	this.window = window;
    	initMaps();
        sizeW = window.getMapSizeW();
        sizeH = window.getMapSizeH();
    	notifyView();
    	new Thread(new Sound("Never_Surrender", 0.01)).start();//musique de fond
    	makeAllTimerTask();
    }
    private void initMaps() {
    	if (!(Load.load)) {
    		maps.put(Constantes.mapBase, new Map(Constantes.mapBase));
    		maps.put(Constantes.mapMaison, new Map(Constantes.mapMaison));		//on crée le dictionnaire de map en initialisant les maps une par une.		
    		maps.put(Constantes.mapMarket, new Map(Constantes.mapMarket));		//et on définit la map sur laquelle le jeu commence et les Objets sur cette même map.
    		maps.put(Constantes.mapRock, new Map(Constantes.mapRock));
    		maps.put(Constantes.mapMaison2, new Map(Constantes.mapMaison2));
    		maps.put(Constantes.mapAttic, new Map(Constantes.mapAttic));
    		for (String s: maps.keySet()) {
    			for (Sums sumsOnMap : maps.get(s).getSumsOnMap()) {			//On crée une liste totale des Sums pour avoir un contrôle plus aisé même sur les Sums hors de la map.
    				sums.add(sumsOnMap);
	    		}
	    	}
	    	currentMap = maps.get(Constantes.mapBase);
	    	this.dog = currentMap.getDog();
	    	objectsOnMap = currentMap.getObjects();
	    	MapDrawer.getInstance().changeMap(currentMap);
	    	for(GameObject o : objectsOnMap) {					//On cherche le premier personnage jouable et on le définit comme le personnage que le joueur contrôle.
	    		if (o instanceof Sums) {
	    			active_player = (Sums) o;
    				changeMap(Constantes.mapBase);
    				window.setPlayer(active_player);
    				break;
    			}
    		}
    	}
    	for(GameObject o : objectsOnMap) {		//On attache des Observers à tous les objets Deletable.
    		if (o instanceof DeletableObject) {
    			((DeletableObject) o).attachDeletable(this);
    		}
    	}
    }

    public void movePlayer(int x, int y, Sums p) {		//On fait bouger le personnage en vérifiant qu'il ne recontre pas un obstacle.
		if (p == null) {
    		p = active_player;
    	}
    	if (p != null && p.isPlayable()) {
	        int nextX = p.getPosX() + x;
	        int nextY = p.getPosY() + y;
	
	        boolean obstacle = false;
	        for (GameObject object : objectsOnMap) {
	            if (object.isAtPosition(nextX, nextY)) {	//On parcourt tous les objets de la map pour voir si un objet est en face du personnage ou non.
	                obstacle = object.isObstacle();			// et pour bloquer le Sums dans ce cas.
	            }
	            if (obstacle == true) {
	                break;
	            }
	        }
	        p.rotate(x, y);
	        if (obstacle == false) {
	            p.move(x, y);
	            if (p == active_player) {		//Le personnage joué se fatigue en marchant.
	            	p.tire();
	            }
	        }
    	}
    	if (p == active_player) {
    		ActionPanel.getInstance().updateVisibleButtons();
    		notifyView();
    	}
    }
    public void moveDog(int x, int y, Dog d) {     //Idem à la fonction précédente mais pour les chiens.
    	if (d != null) {
	        int nextX = d.getPosX() + x;
	        int nextY = d.getPosY() + y;
	
	        boolean obstacle = false;
	        for (GameObject object : objectsOnMap) {
	            if (object.isAtPosition(nextX, nextY)) {
	                obstacle = object.isObstacle();
	            }
	            if (obstacle == true) {
	                break;
	            }
	        }
	        d.rotate(x, y);
	        if (obstacle == false) {
	            d.move(x, y);
	        }
    	}
    	
    }
   
   public void buttonPressed(String button) {
	   if (button != null) {
		   if (button.contentEquals("GIVE FLOWER")) { 
			   GameObject o = active_player.getObjects().get(indexInventory);
			   ((ActivableObject)o).activate(active_player);
			   ((Adult) getFrontObject()).receiveFlower(active_player);}
		   else if (button.contentEquals("MAKE LOVE")) { ((Adult) getFrontObject()).makeLove();}       // Cette fonction s'occupe de gérer tous les bouttons.
		   else if (button.contentEquals("GO TO WORK")) { sendPlayerToWork();}							//Elle regarde d'abord quel boutton a été pressé
		   else if (button.contentEquals("STOCK")) { 													//si aucun bouttons nécessitant des actions particulières
			   GameObject o = active_player.getObjects().get(indexInventory);							//n'a été pressé, elle active l'objet en face du personnage si il existe,
			   active_player.getObjects().remove(o);													// et elle active l'objet pointé dans l'inventaire sinon.
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
	   }
	   else {
		   if (getFrontObject() != null && getFrontObject() instanceof ActivableObject) {
			   getFrontObject().activate(active_player);
		   }
	   }
	   MapDrawer.getInstance().updateContent();
	   MapDrawer.getInstance().requestFocusInWindow();			//On update tous les panels ayant changé.
	   ActionPanel.getInstance().updateVisibleButtons();
	   InventoryPanel.getInstance().updateInventory();
   }
   private void sendPlayerToWork() {
	   	Door closestDoor = getClosestDoor(active_player, true);
	   	Sums p = active_player;
	   	Timer timer = new Timer();
	   	TimerTask workTask = new TimerTask() {				//le Personnage va aller vers la porte la plus proche et l'emprunter pour aller travailler
		   public void run() {								// Il reviendra 50 secondes plus tard, fatigué et plein d'argent $.
			   	int posX = closestDoor.getPosX();			// un timer s'occupe de compter ces 50 secondes.
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
       			((AStarThread) threads.get(0)).start();
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
   
   private void makeAllTimerTask(){
       TimerTask repeatedTask = new TimerTask() {			//Initiation de tous les timers et de leurs tâches respectives, faire passer le temps, faire bouger des sums aléatoires, etc...
           public void run() {
           	ActionPanel.getInstance().updateActivableList();
           	notifyView();
           	MapDrawer.getInstance().updateContent();
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
       		new Thread(new Sound("Never_Surrender", 0.15)).start();
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
          		if (s != null && choice == 1 && s.isPlayable()) {
   	        		int posX = randomDoor.getPosX();
   	        		int posY = randomDoor.getPosY();
   	        		switch(String.valueOf(randomDoor.getChar())) {
   	        		case "E" : posX +=1;break;
   	        		case "W" : posX -= 1;break;
   	        		case "S" : posY +=1; if (randomDoor.getPosX() == sizeW && randomDoor.getPosY() == sizeH) {posY -=1;}; break;    //On ne peut pas envoyer le personnage directement sur 
   	        		case "N" : posY += 1; break;																					//la porte car c'est un obstacle. Il faut donc l'envoyer
   	        		case "H" : posY-=1; break;																						//sur la case juste avant, d'où la nécessité des conditions.
   	        		default : posY += 1; break;	
   	        		}
   	        		threads.add(0, new AStarThread(Game.getInstance(), s, posX, posY, randomDoor));
   	        		((AStarThread) threads.get(0)).start();
   	        	}
          		else if (s!= null && s.isPlayable()) {
          			ArrayList<Integer> list = getRandomPosition(currentMap);
          			threads.add(0, new AStarThread(Game.getInstance(), s, list.get(0), list.get(1), null));
   	        		((AStarThread) threads.get(0)).start();
          		}
          	}
          };
          TimerTask movingDogTask = new TimerTask() {
        	  public void run() {
        		  for (AStarThread t : threads) {
          	   		if (t!= null & t.getDog() == dog) {
          	   			t.stopThread();
          	   		}
          	   	}
        		  synchronized(threads) {
	            		ArrayList<Integer> list = getRandomPosition(currentMap);
	            		threads.add(0, new AStarThread(Game.getInstance(), dog, list.get(0), list.get(1), null));
	            	   	((AStarThread) threads.get(0)).start();
        		  }
        	  }
          };
          TimerTask comingTask = new TimerTask() {
          	public void run() {
          		ArrayList<GameObject> list = new ArrayList<GameObject>(getRandomSumsAndDoor());		//Un Sums aléatoire qui est hors de la map revient sur cette dernière.
          		if (list.size()>0) {
   	        		Sums s = (Sums) list.get(0);
   	        		Door door = (Door) list.get(1);
   	        		if (s != null && s.isPlayable()) {
   	        			door.activate(s);
   	        			ArrayList<Integer> position = getRandomPosition(maps.get(door.getDestination()));
   	        			threads.add(0, new AStarThread(Game.getInstance(), s, position.get(0), position.get(1), null));
   	        			((AStarThread) threads.get(0)).start();
   	        		}
          		}
          	}
   };
       addList(repeatedTask, timerTasks, 500,500);
       addList(timeTask, timerTasks,5,5);
       addList(musicTask, timerTasks, 36000,36000);
       addList(moveTask, timerTasks,2500,2500);
       addList(comingTask, timerTasks,5000,5000);
       addList(lifeTask, timerTasks, 5000,5000);
       addList(movingDogTask, timerTasks, 5000,5000);
       for (int i = 0; i < timerTasks.size(); i+=3) {
       	timers.add(new Timer());
       	TimerTask timerTask = (TimerTask) timerTasks.get(i);
       	long first = (long) timerTasks.get(i+1);
       	long delay = (long) timerTasks.get(i+2);
       	timers.get(i/3).scheduleAtFixedRate(timerTask, first, delay);
       }
  }
  private void addList(TimerTask timerTask, ArrayList<Object> timerTasksList, long first, long delay) {		//On ajoute les TimeTask et leurs délais à une liste pour les manipuler plus facilement.
	   timerTasksList.add(timerTask);
	   timerTasksList.add(first);
	   timerTasksList.add(delay);
  }
  private ArrayList<GameObject> getRandomSumsAndDoor(){					//Cette fonction renvoie un Sums et une porte aléatoire sur la carte.
 		ArrayList<Map> mapList = new ArrayList<Map>(maps.values());		//En vérifiant que ce personnage ne bouge pas déjà en suivant un thread.
		Collections.shuffle(mapList);
		Sums res = null;
		Door door = null;
		ArrayList<GameObject> resList = new ArrayList<GameObject>();
		boolean isAlreadyMoving = false;
		for (Map map: mapList) {
			for (GameObject o : map.getObjects()) {
				if (o instanceof Sums && res == null && o != active_player) {
					synchronized(threads) {
						for (AStarThread t : threads) {
							if (t!= null & t.getSums() == o && t.isNotFinished()){
								isAlreadyMoving = true;
							}
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
 		ArrayList<GameObject> lookingForDoors = new ArrayList<GameObject>(objectsOnMap);	//Cette fonction renvoie une porte aléatoire sur la map.
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
 	private ArrayList<Integer> getRandomPosition(Map map) {			//Cette fonction renvoie une position libre aléatoire sur la map.
 		Random rand = new Random();
 		int randX = rand.nextInt(map.getSizeW());
 		int randY = rand.nextInt(map.getSizeH());
 		ArrayList<Integer> position = new ArrayList<Integer>();
 		int y;
 		int x;
 		boolean [][] positionTaken = map.getPositionTaken();
 		if (positionTaken[randX][randY]){						//Vérifie que la position est libre et non bloquée.
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
    private Door getClosestDoor(Sums s, boolean onlyMapDoors) {		//Calcule la porte la plus proche du Sums en permettant de choisir si on veut les portes sur les bords de map ou toutes les portes
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
    private Sums getRandomSums() {			//Renvoie un Sums aléatoire sur la map n'ayant pas déjà de Thread le faisant bouger.
    	Sums res = null;
    	Collections.shuffle(objectsOnMap);
    	for (GameObject o : objectsOnMap) {
        	boolean isAlreadyMoving = false;
    		if (o instanceof Sums && o != active_player) {
    			try {
	    			for (AStarThread t : threads) {
	    				if (t.getSums() == o && t.isNotFinished()) {
	    					isAlreadyMoving = true;
	    				}
	    			}
    			}
    			catch(ConcurrentModificationException e) {
    				e.printStackTrace();
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
    public void playerWait(long delay, Sums s, String type) {	//Fais attendre le personnage un certain temps l'empêchant de bouger et passant au personnage suivant.
    	if (s == active_player) {
    		setNextActivePlayer(s);
    	}
    	s.setIsPlayable(false);
    	int x = s.getPosX();
		int y = s.getPosY();
    	if (type == "TOILET") {
    		Thread threadSound = new Thread (new Sound("pee",10, Math.round(delay)));
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
    
    private void setNextActivePlayer(Sums s) {			//Met un autre personnage comme personnage actif.
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
    public void action() { 
    	String button = ActionPanel.getInstance().getFirstVisibleButton();	//la barre d'espace correspond au premier boutton visible.
    	buttonPressed(button);
    }

    private void notifyView() {
        window.update();
        window.revalidate();
    }
    public void playerPos() {
        System.out.println(active_player.getPosX() + ":" + active_player.getPosY());   
    }
	public void stop() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
	public void sendPlayer(int x, int y) {		// Selection d'un autre joueur actif.
		for (GameObject p: objectsOnMap) {
			if (p instanceof Sums && x == p.getPosX() && y == p.getPosY()) {
				setActivePlayer((Sums)p);
				MapDrawer.getInstance().requestFocusInWindow();
			}
		}
	}
	private void timePassed() {
		ArrayList<Sums> newList = new ArrayList<Sums>(sums);	//Le temps passe pour tous les Sums.
		for (Sums e: newList) {
			if (e.isPlayable()) {
				e.timePassed();
			}
		}
		notifyView();
	}
	public void sumsEvolution(Sums s, HashMap<Sums, Integer> loveHasMap) {		//Si un Sums passe un âge et change de classe, cette fonction reprend ses objets et recréer un personnage
		ArrayList<GameObject> inventory = s.getInventory();						// avec les bons objets.
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
		if (s == active_player) {setActivePlayer(newSums);}			//Le joueur actif est update si c'est ce dernier qui évolue.
		window.setGameObjects(objectsOnMap);
		ActionPanel.getInstance().updateActivableList();
	}
	public void playerDied(Sums e) {			//Si un joueur meurt...RIP.
		sums.remove(e);							//Si c'est le joueur actif on va chercher un sums random sur toutes les maps.
		e.getMap().getObjects().remove(e);
		if (e == active_player) {
			Random r = new Random();
			int index = r.nextInt(sums.size());
			setActivePlayer(sums.get(index));
			if (active_player.getMap() != currentMap ) {
				changeMap(active_player.getStringMap());
			}
		}
		window.setGameObjects(objectsOnMap);
		ActionPanel.getInstance().updateActivableList();
	}
	public void changeMap(String s) {		//La fonction stoppe tous les threads lors de changement de map.
		for (AStarThread  t : threads) {	//Elle lance l'initialisation des maps maison si c'est le premier passage sur ces maps.
			if (t != null) {				//Les variables de maps sont réinitialisées.
				t.stopThreadChangeMap();
			}
		}
		threads.removeAll(threads);
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
	@Override
    synchronized public void delete(Deletable ps) {
        objectsOnMap.remove(ps);
        if (ps instanceof Sums) {sums.remove(ps);}
        ActionPanel.getInstance().updateActivableList();
        notifyView();
    }
	public ActivableObject getFrontObject() {		//Récupère l'objet en face du joueur actif.
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
	public LocalDateTime getTime() {
		return localDateTime;
	}
	public HashMap<String,Map> getMaps(){
		return maps;
	}
	public Map getCurrentMap() {
		return currentMap;
	}
	public void setActivePlayer(Sums s) {		//Set le joueur actif et si ce dernier se déplaçait avec un Thread, on stoppe ce thread.
		active_player = s;
		window.setPlayer(active_player);
		ActionPanel.getInstance().setPlayer(active_player);
		try {
			for (AStarThread t : threads) {
				if (t.getSums() == active_player) {
					t.stopThread();
				}
			}
		}
		catch(ConcurrentModificationException e) {
			e.printStackTrace();
		}
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
	public ArrayList<Timer> getTimers() {
		return timers;
	}
	public void setDog(Dog dog) {
		this.dog = dog;
	}
	public Dog getDog() {
		return dog;
	}
	public ArrayList<Sums> getSums() {
	    return sums;
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
}