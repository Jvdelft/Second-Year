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

import controller.PanelToDrawArrowsListener;
import model.Furniture;
import model.Constantes;

public class PanelToDrawArrows extends JPanel{
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private int numberOfButtons;
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	private transient PanelToDrawArrowsListener listener = new PanelToDrawArrowsListener();
	public PanelToDrawArrows() {
		this.setOpaque(false);
		this.setLayout(box);
		limits.weightx = 1;
		limits.weighty = 1;
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowUp))));
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowDown))));	//On initialise les bouttons.
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowLeft))));
		initButton(new JButton(new ImageIcon((Image)Constantes.imageHashMap.get(Constantes.arrowRight))));
	}
	private void initButton(JButton button) {
		buttons.add(button);
		button.addActionListener(listener);
		button.setBackground(Color.GRAY.darker());
		switch(numberOfButtons) {
		case 0 : limits.gridx = 1; button.setName("up");this.add(button,limits); break;
		case 1 : limits.gridy = 2; button.setName("down");this.add(button,limits); break;
		case 2 : limits.gridx = 0; button.setName("left");limits.gridy = 1; this.add(button,limits); break;
		case 3 : limits.gridx = 2; button.setName("right");this.add(button,limits); break;
		}
		numberOfButtons++;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
