package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Furniture;
import model.Constantes;

public class PanelToDrawArrows extends JPanel implements ActionListener{
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private int numberOfButtons;
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	public PanelToDrawArrows() {
		this.setOpaque(false);
		this.setLayout(box);
		limits.weightx = 1;
		limits.weighty = 1;
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowUp))));
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowDown))));
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowLeft))));
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowRight))));
	}
	private void initButton(JButton button) {
		buttons.add(button);
		button.addActionListener(this);
		button.setBackground(Color.GRAY.darker());
		switch(numberOfButtons) {
		case 0 : limits.gridx = 1; button.setName("up");this.add(button,limits); break;
		case 1 : limits.gridy = 2; button.setName("down");this.add(button,limits); break;
		case 2 : limits.gridx = 0; button.setName("left");limits.gridy = 1; this.add(button,limits); break;
		case 3 : limits.gridx = 2; button.setName("right");this.add(button,limits); break;
		}
		numberOfButtons++;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	public void actionPerformed(ActionEvent arg0) {
		if (((JButton) arg0.getSource()).getName() == "up") {
			((Furniture) MapDrawer.getInstance().getCurrentMap().getLastObjectPlace()).rotate(0, -1);
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
