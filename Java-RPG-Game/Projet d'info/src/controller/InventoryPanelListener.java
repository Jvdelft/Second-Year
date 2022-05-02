package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Game;
import model.Sums;
import view.InventoryPanel;
import view.MapDrawer;
import view.Window;

public class InventoryPanelListener implements ActionListener, ListSelectionListener {
	private int row = 0;
	private JButton up;
	private JButton down;
	private int lastSelectedIndex;
	private JList list;
	private Sums p;
	public void setPlayer(Sums s) {
		p = s;
	}
	public void initListener() {
		up = InventoryPanel.getInstance().getUp();
		down = InventoryPanel.getInstance().getDown();
		list = InventoryPanel.getInstance().getList();
		up.addActionListener(this);
		down.addActionListener(this);
		list.addListSelectionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Window.getInstance().getMapDrawer().requestFocusInWindow();		//On monte dans l'inventaire si il y a une rangée précédente.
		if(e.getSource().equals(up) && row > 0) {
			row --;
		}
		else if (e.getSource().equals(down) && row +1 <= p.getObjects().size()/4) {	//On descend dans l'inventaire si il y a une rangée suivante.
			row ++;
		}
		InventoryPanel.getInstance().updateInventory();
		InventoryPanel.getInstance().setRow(row);
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.getSelectedIndex() == lastSelectedIndex) {
			Game.getInstance().setIndexInventory(list.getSelectedIndex());
			MapDrawer.getInstance().requestFocusInWindow();
		}
		else {
			lastSelectedIndex = list.getSelectedIndex();
		}
	}

}
