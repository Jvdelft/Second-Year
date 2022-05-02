package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Constantes;

public class HelpPanel extends JPanel{
	private JButton button = new JButton("BACK");
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	public HelpPanel() {
		this.setPreferredSize(new Dimension(1920,1080));			//On initialise le panel qui contient les informations d'aide et on construits la grille pour plus de liberté dans le placement.
		button.setPreferredSize(new Dimension(600,150));
		this.setLayout(box);
		limits.weightx = 1;
		limits.weighty = 1;
		limits.anchor = GridBagConstraints.PAGE_END;
		this.add(button, limits);
		button.setFont(new Font("Monotype Corsiva", Font.BOLD, 40));
		button.setForeground(Color.white);
		button.setBackground(Color.BLUE);
	}
	
	public JButton getButton() {
		return button;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Constantes.helpPanel, 0, 0, 1920, 1080, null);
		g.setFont(new Font("Monotype Corsiva", Font.BOLD, 80));
		g.drawString("Do your best :)", 700, 500);
	}
}
