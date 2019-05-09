package model;

import java.util.ConcurrentModificationException;

public class AStarThread extends Thread{
	private Game g;
	private Sums p;
	private Dog d;
	private int x;
	private int y;
	private Door door;
	private String string;
	private boolean exit = false;

	public AStarThread(Game g, Object o, int x, int y, Object object) {
		this.g= g;
		if (o instanceof Sums) {
			this.p = (Sums) o;
		}
		if (o instanceof Dog) {
			this.d = (Dog) o;
	        System.out.println("HEY");
		}
		this.x = x;
		this.y = y;
		if (object instanceof Door) {
			this.door = (Door) object;
		}
		else if (object instanceof String) {
			this.string = (String) object;
		}
	}
	
	@Override
	public void run(){
		int direction = 0;
		while(direction != -1 && !(exit)) {
			try {
				if (d != null) {
					direction = (new AStar(d.getPosX(), d.getPosY(), x, y, g.getGameObjects())).getNextStep();
				}
				if (p != null) {
					direction = (new AStar(p.getPosX(), p.getPosY(), x, y, g.getGameObjects())).getNextStep();
				}
			}
			catch(ConcurrentModificationException e) {
				
			}
			catch(ArrayIndexOutOfBoundsException e) {
				
			}
			if (p!= null) {
				switch (direction) {
					case 0 : g.movePlayer(1,0,p); break;
					case 1 : g.movePlayer(0,-1,p); break;
					case 2 : g.movePlayer(-1,0,p); break;
					case 3 : g.movePlayer(0,1,p); break;
					default : 
						stopThread();
						break;
				}
			}
			if (d != null) {
				switch (direction) {
				case 0 : g.movePlayer(1,0,d); break;
				case 1 : g.movePlayer(0,-1,d); break;
				case 2 : g.movePlayer(-1,0,d); break;
				case 3 : g.movePlayer(0,1,d); break;
				default : 
					stopThread();
					break;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
	}
	public Sums getSums() {
		return p;
	}
	public Dog getDog() {
		return d;
	}
	public void stopThread() {
		this.exit = true;
		if (door != null) {
			if (door.getPosX()-p.getPosX()<2 && door.getPosY()-p.getPosY() <2) {
				door.activate(p);
			}
		}
		if (string == "WORK") {
			p.teleportation(-1, -1);
		   	p.setIsPlayable(false);
		}
		Game.getInstance().getThreads().remove(this);
	}
	public void stopThreadChangeMap() {
		this.exit = true;
		Game.getInstance().getThreads().remove(this);
	}
}
