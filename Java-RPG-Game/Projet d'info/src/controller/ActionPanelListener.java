package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Game;

public class ActionPanelListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton buttonPressed = (JButton) arg0.getSource();
		if (buttonPressed.isValid() && buttonPressed.getLocationOnScreen().getX()>1470) {		//On vérifie que le boutton est bien dans l'ActionPanel.
			Game.getInstance().buttonPressed(buttonPressed.getText());
		}
	}
}
