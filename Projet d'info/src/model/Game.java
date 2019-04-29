package model;

import view.Window;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.omg.CosNaming.IstringHelper;

public class Game implements DeletableObserver, Runnable {
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private ArrayList<Sums> sums = new ArrayList<Sums>();
    private ArrayList<GameObject> ObjectsOnMap = new ArrayList<GameObject>();
    private Sums active_player = null;

    private Window window;
    private int sizeH;
    private int sizeV;
    private int numberOfBreakableBlocks = 40;
    private Thread t2= new Thread(this);
    private static Game GameInstance;

    private Game(Window window) {
        this.window = window;
        sizeH = window.getHMapSize();
        sizeV = window.getVMapSize();
        // Creating one Player at position (1,1)
        if (objects.isEmpty()) {
        	InitGame();
        }
    	notifyView();
    	t2.start();
    	givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect();
        
    }


    public void movePlayer(int x, int y) {
        int nextX = active_player.getPosX() + x;
        int nextY = active_player.getPosY() + y;

        boolean obstacle = false;
        for (GameObject object : objects) {
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
        }
        Window.getInstance().getStatus().getActionPanel().updateVisibleButtons();
        notifyView();
    }
    public void givenUsingTimer_whenSchedulingRepeatedTask_thenCorrect(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
            	Window.getInstance().getStatus().getActionPanel().updateActivableList();
            }
        };
        Timer timer = new Timer("Timer");
         
        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    public void tirePlayer() {
    	active_player.tire();
    	notifyView();
    }
    public void action() {
        ActivableObject aimedObject = null;
		for(GameObject object : objects) {
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
        return this.objects;
    }
    public ArrayList<ActivableObject> getActivableObjects(){
    	ArrayList<ActivableObject> list = new ArrayList<ActivableObject>();
    	for (GameObject object : objects) {
    		if (object instanceof ActivableObject) {
    			list.add((ActivableObject) object);
    		}
    	}
    	return list;
    }
    public void setGameObject(ArrayList<GameObject> o){
    	this.objects = o;
    }

    @Override
    synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
    	Window.getInstance().getStatus().getActionPanel().updateActivableList();
        objects.remove(ps);
        if (loot != null) {
            objects.addAll(loot);
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
		for (Sums p: sums) {
			if (x == p.getPosX() && y == p.getPosY()) {
				active_player = p;
		        window.setPlayer(p);
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
			objects.remove(e);
			if (e == active_player) {
				Random r = new Random();
				int index = r.nextInt(sums.size()) + 1;
				active_player = sums.get(index);
			}
			notifyView();
				}
			}
	public void setObjects(ArrayList<GameObject> g) {
		this.objects = g;
		int i = 0;
		while (!(objects.get(i) instanceof Sums)) {
			i+= 1;
		}
		active_player = (Sums)objects.get(i);
		window.setGameObjects(g);
	}
	public void AddObject(GameObject o) {
		objects.add(o);
	}
	public void InitGame() {
		House h = new House(10,10);
    	Sums p = new Adult(10, 10,h);
    	Sums q = new Kid(5,5,h);
    	Sums r = new Elder(15,15,h);
    	Sums s = new Teen(24,5,h);
    	objects.add(h);
    	objects.add(p);
    	sums.add(p);
    	active_player = p;
    	objects.add(q);
    	sums.add(q);
    	objects.add(r);
    	sums.add(r);
    	objects.add(s);
    	sums.add(s);
    	window.setPlayer(active_player);

    	// Map building
    	for (int i = 0; i < sizeH; i++) {
    		objects.add(new Border(i, 0));
    		objects.add(new Border(i, sizeV - 1));
    		if (i>9) {
    			objects.add(new Border(0, i-10));
    			objects.add(new Border(sizeH - 1, i-10));
    		}
    	}
    	for (int i = 0; i< h.getSizeH(); i++) {
    		objects.add(new Border(h.getPosX(),i+h.getPosY()));
    		objects.add(new Border (h.getPosX()+h.getSizeH()-1,i+h.getPosY()));
    	}
    	for (int i = 0; i< h.getSizeW(); i++) {
    		objects.add(new Border(i+h.getPosX(),h.getPosY()));
    		objects.add(new Border (i+h.getPosX(),h.getPosY()+h.getSizeH()-1));
    	}
    	objects.add(h.getDoor());
    	Random rand = new Random();
    	for (int i = 0; i < numberOfBreakableBlocks/2; i++) {
    		int x = rand.nextInt(sizeH-4) + 2;
    		int y = rand.nextInt(sizeV-4) + 2;
    		Food test = new Apple(x,y);
    		test.attachDeletable(this);
    		objects.add(test);
    	}
    /*for (int i = 0; i < numberOfBreakableBlocks/2; i++) {
        int x = rand.nextInt(sizeH-4) + 2;
        int y = rand.nextInt(sizeV-4) + 2;
        Drinks test = new RedPotion(x,y);
        test.attachDeletable(this);
        objects.add(test);
    }*/
    	objects.add(new Door(Math.round(sizeH/2)-1,0));
    	objects.add(new Door(0,Math.round(sizeV/2)-1));
    	objects.add(new Door(Math.round(sizeH/2)-1,sizeV-1));
    	objects.add(new Door(sizeH-1,Math.round(sizeV/2)-1));
    	objects.add(new Fridge(10,15));
    	objects.add(new Fridge(2,2));
    	objects.add(new Fridge(28,18));

    	window.setGameObjects(this.getGameObjects());
	}
		}