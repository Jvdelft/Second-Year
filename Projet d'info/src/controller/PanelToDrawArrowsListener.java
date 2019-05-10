package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Furniture;
import view.MapDrawer;

public class PanelToDrawArrowsListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((JButton) arg0.getSource()).getName() == "up") {
			((Furniture) MapDrawer.getInstance().getCurrentMap().getLastObjectPlace()).rotate(0, -1);		// Le meuble en train d'être placé est tourné.
		}
		else if (((JButton) arg0.getSource()).getName() == "down") {
			((Furniture) MapDrawer.getInstance().getCurrentMap().getLastObjectPlace()).rotate(0, 1);
		}
		else if (((JButton) arg0.getSource()).getName() == "left") {
			((Furniture) MapDrawer.getInstance().getCurrentMap().getLastObjectPlace()).rotate(-1, 0);
		}
		else if (((JButton) arg0.getSource()).getName() == "right") {
			((Furniture) MapDrawer.getInstance().getCurrentMap().getLastObjectPlace()).rotate(1, 0);
		}
		
	}
}
