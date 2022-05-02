package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ActivableObject;
import model.ContainerObject;
import model.Fridge;
import model.Game;
import model.GameObject;
import model.MarketShelve;
import model.Sums;
import view.MapDrawer;
import view.Window;

public class MapDrawerListener implements ActionListener, ListSelectionListener{
	private Sums active_player;
	private JList content;
	private int row;
	private ContainerObject container;
	private JButton up;
	private JButton down;
	private JButton buttonEAT;
	private String posButton3;
	public MapDrawerListener() {
		MapDrawer mapdrawer = MapDrawer.getInstance();
		this.content = mapdrawer.getContent();
		this.container = mapdrawer.getContainer();
		this.up = mapdrawer.getUp();
		this.down = mapdrawer.getDown();
		this.buttonEAT = mapdrawer.getButtonEat();
		this.posButton3 = mapdrawer.getPos3();
	}
	public void setPos3(String s) {
		this.posButton3 = s;
	}
	public void setPlayer(Sums s) {
		this.active_player = s;
	}
	public void setContainer(ContainerObject o) {
		this.container = o;
	}
    @Override
	public void actionPerformed(ActionEvent arg0) {
		int index = content.getSelectedIndex();
		if (index < 0) {
			index = 0;
		}
		active_player = Window.getInstance().getActivePlayer();
		if (((JButton) arg0.getSource()).getLocationOnScreen().getX() < 1470) {
			if (arg0.getSource().equals(down) && container.getObjectsContained().size() > (row+1)*4) {
				row++;
			}
			else if (arg0.getSource().equals(up) && row >0) {
				row --;
			}
			if (arg0.getActionCommand().contentEquals("CLOSE")) {
				MapDrawer.getInstance().removeDrawContent();
				if (container instanceof MarketShelve) {
					((MarketShelve)container).initObjectContained(((MarketShelve) container).getShelveType());
				}
				active_player.setIsPlayable(true);
			}
			else if (arg0.getActionCommand().contentEquals("EAT IT")) {
				ActivableObject object = (ActivableObject) container.getObjectsContained().get(index);
				object.activate(active_player);
				container.getObjectsContained().remove(object);
			}
			else if (arg0.getActionCommand().contentEquals("TAKE")) {
				GameObject object = container.getObjectsContained().get(index);
				container.getObjectsContained().remove(object);
				active_player.addInInventory(object);
			}
			else if (arg0.getActionCommand().contentEquals("BUY")) {
				GameObject object = container.getObjectsContained().get(index);
				container.getObjectsContained().remove(object);
				active_player.buy(object);
			}
		}
		MapDrawer.getInstance().updateContent();
		MapDrawer.getInstance().setRow(row);
		Game.getInstance().updateVisibleButtons();
	}
    @Override
	public void valueChanged(ListSelectionEvent arg0) {
		int index = content.getSelectedIndex();
		ArrayList<GameObject> objects = container.switchRow(row);
		if (index < 0) {
			index = 0;
		}
		if (index <= objects.size()-1) {
			if (container instanceof Fridge) {
				if (objects.get(index) instanceof ActivableObject && ((ActivableObject) objects.get(index)).getType().contentEquals("EAT")){
					MapDrawer.getInstance().add(buttonEAT, posButton3);
				}
				else {
					try {
						MapDrawer.getInstance().remove(buttonEAT);
						}
					finally {
						}
					}
				}
			}
		}
}