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
import controller.MapDrawerListener;

public class MapDrawer extends JPanel{
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
    private transient String textToPaint;
    private transient PanelToDrawArrows panelToDrawArrows = null;
    private transient ButtonsForPlacingFurniture panelButtons = null;
    private LocalDateTime localDateTime;
    private transient MapDrawerListener listener;
	
    private MapDrawer() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(sizeW*tileSize, sizeH*tileSize));
        this.setBackground(Color.GREEN);
        this.setLayout(new MigLayout());
		content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		content.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		content.setVisibleRowCount(1);
        
        addMouseListener(new MouseListener() {				//La souris est instanciée pour placer les meubles.
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
    @Override
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	g.drawImage(Constantes.background, 0, 0, 1470, 1080, this);
    	drawClock(g);
    	if (currentMap.getTiles() != null) {
	    	drawTiles(g);
    	}
        if (!(objects.isEmpty())) {
        	drawObjects(g);
        }
    }
    private void drawClock(Graphics g) {			//On dessine l'horloge du temps qui avance.
    	g.setColor(Color.BLUE.darker());
        g.drawRect(0,975,400,50);
        g.fillRect(0,975,400,50);
        g.setColor(Color.BLUE);
        g.drawRect(10, 985, 380, 30);
		g.fillRect(10, 985, 380, 30);
		g.setFont(new Font("Monotype Corsiva", Font.BOLD, 30));
		g.setColor(Color.ORANGE);
		g.drawString(localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), 50, 1010);
    	
    }
    private void drawTiles(Graphics g) {			//On dessine les tiles formant le sol de la map.
    	for (int i = 0; i < sizeH; i++) {
        	int lines = i;
            for (int j = 0; j < sizeW; j++) {
                int Tileposx = j * tileSize;
                int Tileposy = i * tileSize;
                g.drawImage(currentMap.getTiles().get(j+lines*sizeW), Tileposx, Tileposy, tileSize, tileSize, this);
            }
        }
    }
    private void drawObjects(Graphics g) {			//On dessine les objects sur la map.
        	for (GameObject object : this.objects) {
        		synchronized(object){
		            int x = object.getPosX();
		            int y = object.getPosY();
		            if (x < 0 || y < 0) {
		            	continue;
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
    	}
    }
    public void changeMap(Map map) {		//On change la map.
    	currentMap = map;
    	sizeH = currentMap.getSizeH();
    	sizeW = currentMap.getSizeW();
    	tileSize = currentMap.getTileSize();
    }

    public void redraw() {
        this.repaint();
    }
	private void removeImage() {
		combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);		//On supprime l'ancienne image pour dessiner la nouvelle.
		graphe = combined.getGraphics();
	}
	public void updateContent(){
		items.setSize(Constantes.itemsNumber);
		try {
		if (container != null) {
			ArrayList<GameObject> listToDraw = container.switchRow(row);		//On update le contenant, on dessine d'abord les cases vides puis on ajoute le sprite de l'objet si il existe.
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
		catch(java.lang.NullPointerException e) {
		}
	}
	public void drawContent(ContainerObject container) {			//On dessine le contenu juste au dessus de l'objet et on le colle au bord de la map si il dépasse.
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
	private void initButton(int width, int height, int posX, int posY) {		//On dessine les flèches juste à coté du contenu.
		Image img = (Image) Constantes.imageHashMap.get(Constantes.arrowUp);
		up = new JButton(new ImageIcon(img));
		up.setBackground(Color.ORANGE.darker());
		img = (Image) Constantes.imageHashMap.get(Constantes.arrowDown);
		down = new JButton(new ImageIcon(img));
		down.setBackground(Color.ORANGE.darker());
		down.addActionListener(listener);
		up.addActionListener(listener);
		down.setName("Down");
		up.setName("Up");
		String pos1 ="pos " + (posX+width+120) + "px " + (posY+Constantes.image_size/2+5) + "px," + "width " + ("35:55:75") + ", height " + ("35:55:75");
		String pos2 = "pos " + (posX+width+120) + "px " + (posY-Constantes.image_size/2+5) + "px," + "width " + ("35:55:75") + ", height " +("35:55:75");
		this.add(down, pos1);
		this.add(up, pos2);
	}
	private void initActionButtons(int width, int height, int posX, int posY) {		//Idem, on dessine les bouttons en dessous du contenu.
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
		listener = new MapDrawerListener();
		listener.setContainer(container);
		buttonTAKE.addActionListener(listener);
		buttonEAT.addActionListener(listener);
		buttonCLOSE.addActionListener(listener);
		buttonBUY.addActionListener(listener);
	}
	private void initJList(int nLabels) {
		ArrayList<GameObject> listToDraw = container.switchRow(row);		//On dessine les cases vides puis les sprites des objects contenus dans les cases.
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
		content.addListSelectionListener(listener);
	}
	public void removeDrawContent() {		//On supprime tous le contenu et les bouttons dessinés.
		container.close();
		this.requestFocusInWindow();
		row = 0;
		container = null;
		items.removeAllElements();
		this.remove(content);
		buttonCLOSE.removeActionListener(listener);
		this.remove(buttonCLOSE);
		try {
			buttonEAT.removeActionListener(listener);
			this.remove(buttonEAT);
			buttonTAKE.removeActionListener(listener);
			this.remove(buttonTAKE);
			buttonBUY.removeActionListener(listener);
			this.remove(buttonBUY);
		}
		finally {
		}
		
		this.remove(up);
		this.remove(down);
		
	}
	public void removeDrawArrows() {		//On supprime les flèches et les bouttons pour placer les meubles.
		try {
			this.remove(panelToDrawArrows);
		}
		catch(NullPointerException e) {
			e.printStackTrace();
		}
		this.remove(panelButtons);
	}
	public void drawArrowsToDirect(GameObject o) {		// On dessine les flèches après le texte et les bouttons encore plus loin.
		panelToDrawArrows = new PanelToDrawArrows();
		panelButtons = new ButtonsForPlacingFurniture();
		int max = Constantes.image_size*3;
		int min = Constantes.image_size*2;
		int pref = Constantes.image_size*5/2;
		String width = Integer.toString(min) + ":" + Integer.toString(pref) + ":" + Integer.toString(max);
		String pos = "pos " + (textToPaint.length()*21) + "px " + 5 + "px," + "width " + (width) + ", height " +(width);
		if (o instanceof Sofa) {
			this.add(panelToDrawArrows, pos);
		}
		pos = "pos " + (this.textToPaint.length()*27) + "px " + (Constantes.image_size-10) + "px," + "width " + (Constantes.image_size*5) + ", height " +(Constantes.image_size);
		this.add(panelButtons,pos);
	}
	public void updateTime() {
		localDateTime = Game.getInstance().getTime();
		redraw();
	}
	public Dimension getDimension() {
    	return new Dimension(sizeW,sizeH);
    }
	public void addMouse(Mouse m) {
		this.mouseController = m;
	}
	public Mouse getMouse() {
		return this.mouseController;
	}
	public void setTextToPaint(String s) {
		textToPaint = s;
	}
	public Map getCurrentMap() {
		return currentMap;
	}
	public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
        this.redraw();
    }
    public ArrayList<GameObject> getObjects() {
    	return objects;
    }
    public static MapDrawer getInstance() {
    	if (instance_mapDrawer == null) {
    		instance_mapDrawer = new MapDrawer();
    	}
    	return instance_mapDrawer;
    }
    public void setRow(int row) {
    	this.row = row;
    }
    public JList getContent() {
    	return content;
    }
    public JButton getUp() {
    	return up;
    }
    public JButton getDown() {
    	return down;
    }
    public String getPos3() {
    	return posButton3;
    }
    public ContainerObject getContainer() {
    	return container;
    }
    public JButton getButtonEat() {
    	return buttonEAT;
    }
}