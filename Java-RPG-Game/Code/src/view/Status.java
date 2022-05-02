package view;

import java.awt.BorderLayout;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import model.Constantes;
import model.Sums;



public class Status extends JPanel {
	private HUD hud = HUD.getInstance();
	private InventoryPanel inventoryPanel = InventoryPanel.getInstance();
	private ActionPanel actionPanel = ActionPanel.getInstance();
	
    public Status() {
    	this.setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(450,1000));			//Le status est le panel contenant tous les panels autre que la map.
    	this.setOpaque(true);
    	hud.setPreferredSize(new Dimension(450,500));
        this.add(hud, BorderLayout.NORTH);
        actionPanel.setPreferredSize(new Dimension(450,300));
        this.add(actionPanel,BorderLayout.CENTER);
        inventoryPanel.setPreferredSize(new Dimension(450,200));
        this.add(inventoryPanel, BorderLayout.SOUTH);
    }

    public void redraw() {
        this.repaint();
    }
	public void setPlayer(Sums p2) {
		hud.setPlayer(p2);
		inventoryPanel.setPlayer(p2);
		actionPanel.setPlayer(p2);
	}
	public InventoryPanel getInventoryPanel() {
		return inventoryPanel;
	}
	public ActionPanel getActionPanel() {
		return actionPanel;
	}
	
}
