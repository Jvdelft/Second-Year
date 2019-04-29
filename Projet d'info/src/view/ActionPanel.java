package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ActivableObject;
import model.Game;
import model.GameObject;
import model.Sums;

public class ActionPanel extends JPanel implements ActionListener {
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	private int x;
	private int y;
	private Image background = Toolkit.getDefaultToolkit().createImage("Images/background.jpg");
	private Font font = new Font("Monotype Corsiva", Font.BOLD, 20);
	private Sums active_player;
	private HashMap buttons = new HashMap();
	private ArrayList<JButton> allButtons = new ArrayList<JButton>();
	private ArrayList<JButton> visibleButtons = new ArrayList<JButton>();
	private ArrayList<ActivableObject> activableObjects;
	public ActionPanel() {
		System.out.println("EY");
		limits.weightx = 1;
		limits.weighty = 1;
		initButton(new JButton("EAT"));
		initButton(new JButton("STOCK"));
		initButton(new JButton("GIVE"));
		initButton(new JButton("TOILET"));
		initButton(new JButton("INTERACT"));
		initButton(new JButton("TAKE"));
		initButton(new JButton("CLOSE"));
		this.setLayout(this.box);
		
	}
	public void initButton(JButton button) {
		button.setFont(font);
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLUE);
		buttons.put(button.getText(), button);
		allButtons.add(button);
		button.addActionListener(this);
	}
	public void updateActivableList() {
		activableObjects = Game.getInstance().getActivableObjects();
	}
	public void updateVisibleButtons() {
		ArrayList<JButton> mustAddButtons = new ArrayList<JButton>();
		if (activableObjects != null) {
			for (ActivableObject object : activableObjects) {
				if (object.getPosX() == active_player.getFrontX() && object.getPosY() == active_player.getFrontY()) {
					String type = object.getType();
					this.addButton(type);
					mustAddButtons.add((JButton) buttons.get(type));
				}
			}
			for (JButton object : allButtons) {
				if (!(mustAddButtons.contains(object))) {
					removeButton(object);
				}
			}
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
	}
	public void actionPerformed(ActionEvent e) {
		if (((JButton) e.getSource()).getLocationOnScreen().getX()>1470) {
			Window.getInstance().getMap().requestFocusInWindow();
			if (e.getActionCommand() == "EAT") {
				for (ActivableObject o : activableObjects) {
					if (o.getPosX() == active_player.getFrontX() && o.getPosY() == active_player.getFrontY()) {
						o.activate(active_player);
					}
				}
			}
		}
			else {
				if (e.getActionCommand() == "CLOSE") {
					Window.getInstance().getMap().removeDrawContent();
				}
			}
	}
	public void setPlayer(Sums s) {
		active_player = s;
	}
	public void removeButton(JButton button) {
		if (visibleButtons.contains(button)) {
			this.remove(button);
			visibleButtons.remove(button);
			x--;
			if (x < 0) {
				y--;
				x +=3;
			}
		}
	}
	public void addButton(String s) {
		JButton button = (JButton) buttons.get(s);
		Boolean notIn = true;
		if (visibleButtons.contains(button)) {
			notIn = false;
		}
		if (y < 3 && notIn) {
			limits.gridx = x;
			limits.gridy = y;
			box.setConstraints(button, limits);
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			button.setAlignmentY(Component.CENTER_ALIGNMENT);
			this.add(button);
			visibleButtons.add(button);
			x++;
			if (x>2) {
				y ++;
				x -= 3;
			}
		}
	}
	public HashMap getButtonsHashMap() {
		return buttons;
	}
}
