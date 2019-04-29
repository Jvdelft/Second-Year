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
	private HUD hud = new HUD();
	private InventoryPanel inventoryPanel = new InventoryPanel();
	private ActionPanel actionPanel = new ActionPanel();
	
    public Status() {
    	this.setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(450,1000));
    	this.setOpaque(true);
    	hud.setPreferredSize(new Dimension(450,500));
        this.add(hud, BorderLayout.NORTH);
        inventoryPanel.setPreferredSize(new Dimension(450,200));
        this.add(inventoryPanel, BorderLayout.SOUTH);
        actionPanel.setPreferredSize(new Dimension(450,300));
        this.add(actionPanel,BorderLayout.CENTER);
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
