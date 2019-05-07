package view;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.Mouse;
import model.*;
import net.miginfocom.swing.MigLayout;
import controller.Keyboard;

public class MapDrawer extends JPanel implements ActionListener, ListSelectionListener{
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    public final int MAP_SIZE = 30;
    private boolean firstMap = true;
    private Mouse mouseController = null;
    private static MapDrawer instance_mapDrawer = null;
    private transient JList content = new JList();
	private DefaultListModel items = new DefaultListModel();
	private transient Image inventoryCase = Constantes.inventoryCase.getScaledInstance(Constantes.image_size+10, Constantes.image_size+10, Image.SCALE_SMOOTH);
	private transient BufferedImage combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
	private transient Graphics graphe = combined.getGraphics();
	private JButton up;
	private JButton down;
	private HashMap buttons = new HashMap();
	private String posButton1;
	private String posButton2;
	private String posButton3;
	private ContainerObject container;
    private int row;
    private Sums active_player;
    private JButton buttonEAT;
    private JButton buttonTAKE;
    private JButton buttonCLOSE;
    private JButton buttonBUY;
    private Map currentMap;
    private int sizeH;
    private int sizeW;
    private int tileSize;
    private String textToPaint;
    private PanelToDrawArrows panelToDrawArrows = null;
    private ButtonsForPlacingFurniture panelButtons = null;
    private LocalDateTime localDateTime;
	
    private MapDrawer() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(sizeW*tileSize, sizeH*tileSize));
        this.setBackground(Color.GREEN);
        this.setLayout(new MigLayout());
		content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		content.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		content.setVisibleRowCount(1);
        
        addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX()/tileSize;
				int y = e.getY()/tileSize;
				mouseController.mapEvent(x, y);
				if (currentMap.isNotInitHouse()) {
					currentMap.initHouse(x,y);
				}
			}
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
    }
    
    public void paintComponent(Graphics g) throws ConcurrentModificationException{
    	super.paintComponent(g);
    	g.drawImage(Constantes.background, 0, 0, 1470, 1080, this);
    	if (currentMap.getTiles() != null) {
	    	for (int i = 0; i < sizeH; i++) {
	        	int lines = i;
	            for (int j = 0; j < sizeW; j++) {
	                int Tileposx = j * tileSize;
	                int Tileposy = i * tileSize;
	 
	                g.drawImage(currentMap.getTiles().get(j+lines*sizeW), Tileposx, Tileposy, tileSize, tileSize, this);
	            }
	        }
    	}
        g.setColor(Color.BLUE.darker());
        g.drawRect(0,975,400,50);
        g.fillRect(0,975,400,50);
        g.setColor(Color.BLUE);
        g.drawRect(10, 985, 380, 30);
		g.fillRect(10, 985, 380, 30);
		g.setFont(new Font("Monotype Corsiva", Font.BOLD, 30));
		g.setColor(Color.ORANGE);
		
		g.drawString(localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), 50, 1010);
    	
        if (!(objects.isEmpty())) {
        	for (GameObject object : this.objects) {
	            int x = object.getPosX();
	            int y = object.getPosY();
	            if (x < 0 || y < 0) {
	            	continue;
	            }
	            else if (object instanceof ActivableObject) {
	            	g.drawImage(object.getSprite(), x*tileSize, y*tileSize,  object.getSizeW()*tileSize,object.getSizeH()*tileSize, this);
	            }
	            else if(object instanceof Sums) {
	            	drawSprites((Sums) object, g);
	            }
	            else {
	            	g.drawImage(object.getSprite(), x*tileSize, y*tileSize,  object.getSizeW()*tileSize,object.getSizeH()*tileSize, this);
	            }
	            if (textToPaint != null) {
	            	g.setColor(Color.WHITE);
	            	g.drawRect(0, tileSize/8, textToPaint.length()*20, tileSize/2);
	            	g.fillRect(0, tileSize/8, textToPaint.length()*20, tileSize/2);
	            	g.setColor(Color.BLACK);
	            	g.setFont(new Font("Monotype Corsiva", Font.BOLD, 40));
	            	g.drawString(textToPaint, tileSize/2, tileSize/2);
	            }
        	}
            /*else if (object instanceof Building) {
            	g.drawImage(object.getSprite(), x*tileSize, y*tileSize, ((Building) object).getSizeH()*tileSize,((Building) object).getSizeV()*tileSize, this);
            }
            else{
            	g.drawImage(object.getSprite(), x*tileSize, y*tileSize, tileSize, tileSize, this);
            }*/
        }
    }
    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
        this.redraw();
    }
    public ArrayList<GameObject> getObjects() {
    	return objects;
    }
    public void drawSprites(Sums s, Graphics g) {
    	int x = s.getPosX();
        int y = s.getPosY();
    	g.drawImage(s.getSprite(), x*tileSize, y*tileSize, tileSize, tileSize, this);
    }
    public static MapDrawer getInstance() {
    	if (instance_mapDrawer == null) {
    		instance_mapDrawer = new MapDrawer();
    	}
    	return instance_mapDrawer;
    }

    public void redraw() {
        this.repaint();
    }
    public Dimension getDimension() {
    	return new Dimension(sizeW,sizeH);
    }
    public void changeMap(Map map) {
    	currentMap = map;
    	sizeH = currentMap.getSizeH();
    	sizeW = currentMap.getSizeW();
    	tileSize = currentMap.getTileSize();
    }

	public void addMouse(Mouse m) {
		this.mouseController = m;
	}
	public Mouse getMouse() {
		return this.mouseController;
	}
	/*public void drawTimeRemaining(int time, Sums s) {
	    posX = s.getPosX() + Constantes.image_size;
		posY = s.getPosY()+Constantes.image_size;
		int size = Math.round(Constantes.image_size/2);
		removeImage();
		graphe.drawImage(Constantes.bubbleThought, 0,0,null);
		graphe.setFont(new Font("Monotype Corsiva", Font.BOLD, 20));
		graphe.drawString(Integer.toString(time), size, size);
	}*/
	private void removeImage() {
		combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
		graphe = combined.getGraphics();
	}
	public void updateContent(){
		items.setSize(Constantes.itemsNumber);
		if (container != null) {
			ArrayList<GameObject> listToDraw = container.switchRow(row);
			for (int i = 0; i < Constantes.itemsNumber;i++) {
				removeImage();
				graphe.drawImage(inventoryCase, 0, 0, null);
				if (i < listToDraw.size()) {
					Image img = (Image) listToDraw.get(i).getSprite();
					graphe.drawImage(img, 5, 5, Constantes.image_size, Constantes.image_size, null);
				}
				items.setElementAt(new ImageIcon(combined), i);
			}
			content.setModel(items);
		}
	}
	public void drawContent(ContainerObject container) {
		this.container = container;
		int posX;
		int width = 220;
		int height = 80;
		if (container.getPosX()*tileSize-Constantes.image_size-90 <0) {
			posX = 0;
		}
		else if (container.getPosX()*tileSize-Constantes.image_size-90 > this.getWidth()-width*2) {
			posX = 1470-width*2;
		}
		else {
			posX = container.getPosX()*tileSize-Constantes.image_size-90;
		}
		int posY;
		if ((container.getPosY()-2)*tileSize <= 0) {
			posY = 30;
		}
		else if ((container.getPosY()-2)*tileSize > this.getHeight()-2*height) {
			posY = this.getHeight()-2*height;
		}
		else {
			posY = (container.getPosY()-2)*tileSize;
		}
		initJList(Constantes.itemsNumber);
		content.setModel(items);
		this.add(content, ("pos " + posX + "px " + posY + "px," + "width " + width + ", height " + height ));
		initButton(width,height,posX,posY);
		initActionButtons(width,height,posX,posY);
		
		
	}
	private void initButton(int width, int height, int posX, int posY) {
		Image img = (Image) Constantes.imageHashMap.get(Constantes.arrowUp);
		up = new JButton(new ImageIcon(img));
		up.setBackground(Color.ORANGE.darker());
		img = (Image) Constantes.imageHashMap.get(Constantes.arrowDown);
		down = new JButton(new ImageIcon(img));
		down.setBackground(Color.ORANGE.darker());
		down.addActionListener(this);
		up.addActionListener(this);
		down.setName("Down");
		up.setName("Up");
		String pos1 ="pos " + (posX+width+120) + "px " + (posY+Constantes.image_size/2+5) + "px," + "width " + ("35:55:75") + ", height " + ("35:55:75");
		String pos2 = "pos " + (posX+width+120) + "px " + (posY-Constantes.image_size/2+5) + "px," + "width " + ("35:55:75") + ", height " +("35:55:75");
		this.add(down, pos1);
		this.add(up, pos2);
	}
	private void initActionButtons(int width, int height, int posX, int posY) {
		buttons =ActionPanel.getInstance().getButtonsHashMap();
		buttonEAT = (JButton) buttons.get("EAT IT");
		buttonTAKE = (JButton) buttons.get("TAKE");
		buttonCLOSE = (JButton) buttons.get("CLOSE");
		buttonBUY = (JButton) buttons.get("BUY");
		int widthButton = width/4;
		int heightButton = height/3;
		posButton1 =  "pos " + (posX) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		posButton2 = "pos " + (posX+widthButton*2) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		posButton3 = "pos " + (posX+widthButton*4) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		if (container instanceof Fridge) {
			this.add(buttonTAKE, posButton2);
		}
		else if (container instanceof MarketShelve) {
			this.add(buttonBUY, posButton2);
		}
		this.add(buttonCLOSE, posButton1);
		buttonTAKE.addActionListener(this);
		buttonEAT.addActionListener(this);
		buttonCLOSE.addActionListener(this);
		buttonBUY.addActionListener(this);
	}
	private void initJList(int nLabels) {
		ArrayList<GameObject> listToDraw = container.switchRow(row);
		for (int i = 0; i < Constantes.itemsNumber;i++) {
			removeImage();
			graphe.drawImage(inventoryCase, 0, 0, null);
			if (i < listToDraw.size()) {
				Image img = (Image) listToDraw.get(i).getSprite();
				graphe.drawImage(img, 5, 5,Constantes.image_size,Constantes.image_size, null);
			}
			items.add(i, new ImageIcon(combined));
		}
		content.setModel(items);
		content.addListSelectionListener(this);
	}
	public void removeDrawContent() {
		items.removeAllElements();
		this.remove(content);
		buttonCLOSE.removeActionListener(this);
		this.remove(buttonCLOSE);
		try {
			buttonEAT.removeActionListener(this);
			this.remove(buttonEAT);
			buttonTAKE.removeActionListener(this);
			this.remove(buttonTAKE);
			buttonBUY.removeActionListener(this);
			this.remove(buttonBUY);
		}
		finally {
		}
		
		this.remove(up);
		this.remove(down);
		container.close();
		this.requestFocusInWindow();
		row = 0;
		
	}
	public void removeDrawArrows() {
		this.remove(panelToDrawArrows);
		this.remove(panelButtons);
	}
	public void drawArrowsToDirect(GameObject o) {
		System.out.println(o);
		if (panelToDrawArrows == null) {
			panelToDrawArrows = new PanelToDrawArrows();
		}
		if (panelButtons == null) {
			panelButtons = new ButtonsForPlacingFurniture();
		}
		int max = Constantes.image_size*3;
		int min = Constantes.image_size*2;
		int pref = Constantes.image_size*5/2;
		String width = Integer.toString(min) + ":" + Integer.toString(pref) + ":" + Integer.toString(max);
		System.out.println(textToPaint);
		String pos = "pos " + (textToPaint.length()*21) + "px " + 5 + "px," + "width " + (width) + ", height " +(width);
		this.add(panelToDrawArrows, pos);
		pos = "pos " + (textToPaint.length()*27) + "px " + (Constantes.image_size-10) + "px," + "width " + (Constantes.image_size*5) + ", height " +(Constantes.image_size);
		this.add(panelButtons,pos);
	}
	public void updateTime() {
		localDateTime = Game.getInstance().getTime();
		redraw();
	}

	public void actionPerformed(ActionEvent arg0) {
		int index = content.getSelectedIndex();
		if (index < 0) {
			index = 0;
		}
		active_player = Window.getInstance().getActivePlayer();
		if (((JButton) arg0.getSource()).getLocationOnScreen().getX() < 1470) {
			if (arg0.getSource().equals(down) && container.getObjectsContained().size() > (row+1)*4) {
				row++;
				updateContent();
			}
			else if (arg0.getSource().equals(up) && row >0) {
				row --;
				updateContent();
			}
			if (arg0.getActionCommand() == "CLOSE") {
				removeDrawContent();
				if (container instanceof MarketShelve) {
					((MarketShelve)container).initObjectContained(((MarketShelve) container).getShelveType());
				}
				active_player.setIsPlayable(true);
			}
			else if (arg0.getActionCommand() == "EAT IT") {
				ActivableObject object = (ActivableObject) container.getObjectsContained().get(index);
				object.activate(active_player);
				container.getObjectsContained().remove(object);
				updateContent();
			}
			else if (arg0.getActionCommand() == "TAKE") {
				GameObject object = container.getObjectsContained().get(index);
				container.getObjectsContained().remove(object);
				active_player.addInInventory(object);
				updateContent();
			}
			else if (arg0.getActionCommand() == "BUY") {
				GameObject object = container.getObjectsContained().get(index);
				container.getObjectsContained().remove(object);
				active_player.buy(object);
				updateContent();
			}
		}
		ActionPanel.getInstance().updateVisibleButtons();
	}
	public void valueChanged(ListSelectionEvent arg0) {
		int index = content.getSelectedIndex();
		ArrayList<GameObject> objects = container.switchRow(row);
		if (index < 0) {
			index = 0;
		}
		if (index <= objects.size()-1) {
			if (container instanceof Fridge) {
				if (objects.get(index) instanceof ActivableObject && ((ActivableObject) objects.get(index)).getType() == "EAT"){
					this.add(buttonEAT, posButton3);
				}
				else {
					try {
						this.remove(buttonEAT);
					}
					finally {
					}
				}
			}
		}
	}
	public void setTextToPaint(String s) {
		textToPaint = s;
	}
	public Map getCurrentMap() {
		return currentMap;
	}
	}