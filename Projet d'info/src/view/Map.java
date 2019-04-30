package view;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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

public class Map extends JPanel implements ActionListener, ListSelectionListener{
    private ArrayList<GameObject> objects = null;
    public final int MAP_SIZE = 30;
    private Mouse mouseController = null;
    public int tileVerticale = 20;
    public int tileHorizontale = 30;
    private int tileSize = 48;
    private MapReader tiles;
    public String map = Constantes.MapBase;
    private boolean firstMap = true;
    private MapReader pixels = new MapReader();
    private static Map instance_map = null;
    private JList content = new JList();
	private DefaultListModel items = new DefaultListModel();
	private Image inventoryCase = Constantes.inventoryCase.getScaledInstance(Constantes.image_size+10, Constantes.image_size+10, Image.SCALE_SMOOTH);
	private BufferedImage combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
	private Graphics graphe = combined.getGraphics();
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
	
    private Map() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(tileHorizontale*tileSize, tileVerticale*tileSize));
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
			}
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
    }
    
    public void paintComponent(Graphics g) {
    	g.drawImage(Constantes.background, 0, 0, 1470, 1080, this);
    	if (firstMap) {
    		this.changeMap(Constantes.MapBase);
    		firstMap = false;
    	}
        for (int i = 0; i < tileVerticale; i++) {
        	int lines = i;
            for (int j = 0; j < tileHorizontale; j++) {
                int Tileposx = j * tileSize;
                int Tileposy = i * tileSize;
 
                g.drawImage(pixels.tiles.get(j+lines*tileHorizontale), Tileposx, Tileposy, tileSize, tileSize, this);
            }
        }
    	

        for (GameObject object : this.objects) {
            int x = object.getPosX();
            int y = object.getPosY();
            if (x < 0 || y < 0) {
            	continue;
            }
            else if(object instanceof Sums) {
            	drawSprites((Sums) object, g);
            }
            else if (object instanceof Building) {
            	g.drawImage(object.Sprite, x*tileSize, y*tileSize, ((Building) object).getSizeW()*tileSize,((Building) object).getSizeH ()*tileSize, this);
            }
            else {
            	g.drawImage(object.Sprite, x*tileSize, y*tileSize, tileSize, tileSize, this);
            // Decouper en fontions
                

                //int xCenter = x * tileSize + (tileSize-2)/2;
                //int yCenter = y * tileSize + (tileSize-2)/2;
                //g.drawLine(xCenter, yCenter, xCenter + deltaX, yCenter + deltaY);
            }
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
    	int direction = s.getDirection();
        
        switch (direction) {
        case Directable.EAST:
            g.drawImage(s.Sprite_r, x*tileSize, y*tileSize, tileSize, tileSize, this);
            break;
        case Directable.NORTH:
        	g.drawImage(s.Sprite_u, x*tileSize, y*tileSize, tileSize, tileSize, this);
            break;
        case Directable.WEST:
        	g.drawImage(s.Sprite_l, x*tileSize, y*tileSize, tileSize, tileSize, this);
            break;
        case Directable.SOUTH:
        	g.drawImage(s.Sprite_d, x*tileSize, y*tileSize, tileSize, tileSize, this);
            break;
        }
    	
    }
    public static Map getInstance() {
    	if (instance_map == null) {
    		instance_map = new Map();
    	}
    	return instance_map;
    }

    public void redraw() {
        this.repaint();
    }
    public Dimension getDimension() {
    	return new Dimension(tileHorizontale,tileVerticale);
    }
    public void changeMap(String Change) {
    	this.map = Change;
    	pixels.ReadMap(map);
    	tileHorizontale = pixels.hTiles; //probl�me avec pixels 
		tileVerticale = (tileHorizontale * 2)/3 ;
		tileSize = 1440 / tileHorizontale;
    }

	public void addMouse(Mouse m) {
		this.mouseController = m;
	}
	private void removeImage() {
		combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
		graphe = combined.getGraphics();
	}
	public void updateContent(){
		ArrayList<GameObject> listToDraw = container.switchRow(row);
		for (int i = 0; i < Constantes.itemsNumber;i++) {
			removeImage();
			graphe.drawImage(inventoryCase, 0, 0, null);
			if (i < listToDraw.size()) {
				Image img = (Image) Constantes.imageHashMap.get(listToDraw.get(i).Sprite);
				graphe.drawImage(img, 5, 5, null);
			}
			items.setElementAt(new ImageIcon(combined), i);
		}
		content.setModel(items);
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
		int widthButton = width/4;
		int heightButton = height/3;
		posButton1 =  "pos " + (posX) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		posButton2 = "pos " + (posX+widthButton*2) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		posButton3 = "pos " + (posX+widthButton*4) + "px " + (posY+height+heightButton) + "px," + "width " + widthButton + ", height " +heightButton;
		this.add(buttonTAKE, posButton2);
		this.add(buttonCLOSE, posButton1);
		buttonTAKE.addActionListener(this);
		buttonEAT.addActionListener(this);
		buttonCLOSE.addActionListener(this);
		
	}
	private void initJList(int nLabels) {
		ArrayList<GameObject> listToDraw = container.switchRow(row);
		for (int i = 0; i < Constantes.itemsNumber;i++) {
			removeImage();
			graphe.drawImage(inventoryCase, 0, 0, null);
			if (i < listToDraw.size()) {
				Image img = (Image) Constantes.imageHashMap.get(listToDraw.get(i).Sprite);
				graphe.drawImage(img, 5, 5, null);
			}
			items.add(i, new ImageIcon(combined));
		}
		content.setModel(items);
		content.addListSelectionListener(this);
	}
	public void removeDrawContent() {
		this.remove(content);
		this.remove(buttonTAKE);
		this.remove(buttonCLOSE);
		try {
			this.remove(buttonEAT);
		}
		finally {
		}
		
		this.remove(up);
		this.remove(down);
		items.removeAllElements();
		this.requestFocusInWindow();
		row = 0;
		
	}

	public void actionPerformed(ActionEvent arg0) {
		int index = content.getSelectedIndex();
		if (index < 0) {
			index = 0;
		}
		active_player = Window.getInstance().getActivePlayer();
		if (((JButton) arg0.getSource()).getLocationOnScreen().getX() < 1470) {
			if (arg0.getSource().equals(down)) {
				row++;
				updateContent();
			}
			else if (arg0.getSource().equals(up) && row >0) {
				row --;
				updateContent();
			}
			if (arg0.getActionCommand() == "CLOSE") {
				removeDrawContent();
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
				active_player.getObjects().add(object);
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
