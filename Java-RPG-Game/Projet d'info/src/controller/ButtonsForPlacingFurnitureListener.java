package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Game;
import model.Map;

public class ButtonsForPlacingFurnitureListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Map map = Game.getInstance().getCurrentMap();
		if (((JButton)  arg0.getSource()).getText() == "DONE") {
			map.placeNextObject();
		}
		else {
			map.getObjects().remove(map.getLastObjectPlace());
			map.setLastObjectPlace(null);
		}
		
	}

}
