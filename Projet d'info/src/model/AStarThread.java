package model;

public class AStarThread extends Thread{
	private Game g;
	private Sums p;
	private int x;
	private int y;
	private Door door;
	private String string;
	private boolean exit = false;

	public AStarThread(Game g, Sums p, int x, int y, Object object) {
		this.g= g;
		this.p = p;
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
		synchronized(p) {
		while(direction != -1 && !(exit)) {
			direction = (new AStar(p.getPosX(), p.getPosY(), x, y, g.getGameObjects())).getNextStep();
			switch (direction) {
				case 0 : g.movePlayer(1,0,p); break;
				case 1 : g.movePlayer(0,-1,p); break;
				case 2 : g.movePlayer(-1,0,p); break;
				case 3 : g.movePlayer(0,1,p); break;
				default : 
					stopThread();
					break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
	public Sums getSums() {
		return p;
	}
	public void stopThread() {
		this.exit = true;
		if (door != null) {
			door.activate(p);
		}
		if (string == "WORK") {
			p.teleportation(-1, -1);
		   	p.setIsPlayable(false);
		}
	}
}
