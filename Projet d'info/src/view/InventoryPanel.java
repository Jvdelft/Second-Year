package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Constantes;
import model.Game;
import model.GameObject;
import model.Sums;

import java.util.ArrayList;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class InventoryPanel extends JPanel implements ActionListener, ListSelectionListener{
	private JList list = new JList();
	private Sums p = null;
	private int row;
	private int itemsNumber = Constantes.itemsNumber;
	private DefaultListModel items = new DefaultListModel();
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	private BufferedImage combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
	private Graphics g = combined.getGraphics();
	private JButton up;
	private JButton down;
	private Image inventoryCase = Constantes.inventoryCase.getScaledInstance(Constantes.image_size+10, Constantes.image_size+10, Image.SCALE_SMOOTH);
	private static InventoryPanel inventoryPanel_instance;
	private InventoryPanel() {
		Constantes.makeList();
		limits.weightx = 1;
		limits.weighty = 1;
		initInventory();
		this.setLayout(this.box);
		initLabels(itemsNumber);
		initButtons();
		}
	private void removeImage() {
		combined = new BufferedImage(Constantes.image_size+10,Constantes.image_size+10,BufferedImage.TYPE_INT_ARGB);
		g = combined.getGraphics();
	}
	public void updateInventory() {
		for (int i = 0; i < items.size();i++) {
			removeImage();
			g.drawImage(inventoryCase, 0, 0, null);
			if (p.getObjects().size() > i+row*4) {
				Image img = (Image) Constantes.imageHashMap.get(p.getObjects().get(i+row*4).getSprite());
				g.drawImage(img, 5, 5, null);
			}
			items.setElementAt(new ImageIcon(combined), i);
		}
		list.setModel(items);
	}
	public void initInventory() {
		limits.gridwidth = 2;
		limits.gridheight = 2;
		limits.gridx = 0;
		limits.gridy = 3;
		list.setBounds(new Rectangle(210,100));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		box.setConstraints(list, limits);
		this.add(list);
		limits.gridheight = 1;
		limits.gridwidth = 1;
		limits.gridx = 2;
		limits.gridy = 3;
		list.addListSelectionListener(this);
	}
	private void initLabels(int nLabels) {
		for (int i = 0; i<nLabels;i++) {
			Image img = inventoryCase;
			g.drawImage(img, 0, 0, null);
			items.add(i,new ImageIcon(combined));
		}
		list.setModel(items);
	}
	private void initButtons() {
		Image img = (Image) Constantes.imageHashMap.get(Constantes.arrowUp);
		up = new JButton(new ImageIcon(img));
		up.setBackground(Color.ORANGE);
		img = (Image) Constantes.imageHashMap.get(Constantes.arrowDown);
		down = new JButton(new ImageIcon(img));
		down.setBackground(Color.ORANGE);
		up.setPreferredSize(new Dimension(Constantes.image_size,Constantes.image_size));
		down.addActionListener(this);
		up.addActionListener(this);
		down.setName("Down");
		up.setName("Up");
		down.setPreferredSize(new Dimension(Constantes.image_size,Constantes.image_size));
		box.setConstraints(up, limits);
		this.add(up);
		limits.gridy = 4;
		box.setConstraints(down, limits);
		this.add(down);
	}
	public void actionPerformed(ActionEvent e) {
		Window.getInstance().getMapDrawer().requestFocusInWindow();
		if(e.getSource().equals(up) && row > 0) {
			row --;
		}
		else if (e.getSource().equals(down) && row +1 <= p.getObjects().size()/4) {
			row ++;
		}
		updateInventory();
	}
	public void setPlayer(Sums p2) {
		this.p = p2;
		if (this.p != null) {
			updateInventory();
		}
	}
	public void valueChanged(ListSelectionEvent e) {
		ActionPanel.getInstance().setSelectedIndexInventory(list.getSelectedIndex());
		MapDrawer.getInstance().requestFocusInWindow();
		ActionPanel.getInstance().updateVisibleButtons();
	}
	public static InventoryPanel getInstance() {
		if (inventoryPanel_instance == null) {
			inventoryPanel_instance = new InventoryPanel();
		}
		return inventoryPanel_instance;
	}
}