package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Constantes;

public class MainMenu extends JPanel{
	private JButton NewGame = new JButton("NEW GAME");
	private JButton Exit = new JButton("EXIT");
	private JButton Continue = new JButton("CONTINUE");
	private GridBagLayout box;
	private GridBagConstraints limits = new GridBagConstraints();
	private ArrayList<JButton> Buttons = new ArrayList<JButton>();
	public MainMenu() {
		
		this.box = new GridBagLayout();
		this.box.preferredLayoutSize(this);
		this.setLayout(this.box);
		limits.weightx = 50;
		limits.weighty = 50;
		initButton(NewGame);
		initButton(Continue);
		initButton(Exit);
	}
	public void paint(Graphics g) {
		g.drawImage(Constantes.menu, 0, 0, 1920, 1080, this);
		
	}
	public ArrayList<JButton> getButtons(){
		return Buttons;
	}
	private void initButton(JButton b) {
		Buttons.add(b);
		b.setFont(new Font("Monotype Corsiva", Font.BOLD, 40));
		b.setBackground(Color.green.darker());
		limits.gridwidth = GridBagConstraints.REMAINDER;
		this.add(b);
		box.setConstraints(b, limits);
		b.setPreferredSize(new Dimension(600,150));
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.setAlignmentY(Component.CENTER_ALIGNMENT);
	}
	
}
