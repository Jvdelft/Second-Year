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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();
	private ArrayList<JButton> allButtons = new ArrayList<JButton>();
	private ArrayList<JButton> visibleButtons = new ArrayList<JButton>();
	private ArrayList<ActivableObject> activableObjects;
	private int index;
	private static ActionPanel actionPanel_instance;
	private ActionPanel() {
		limits.weightx = 1;
		limits.weighty = 1;
		initButton(new JButton("EAT"));
		initButton(new JButton("STOCK"));
		initButton(new JButton("GIVE"));
		initButton(new JButton("TOILET"));
		initButton(new JButton("INTERACT"));
		initButton(new JButton("TAKE"));
		initButton(new JButton("CLOSE"));
		initButton(new JButton("OPEN"));
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
			if (active_player.getObjects().size() >0) {
				if (((ActivableObject) active_player.getObjects().get(index)).getType() == "EAT"){
					this.addButton("EAT");
					mustAddButtons.add(buttons.get("EAT"));
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
		JButton buttonPressed = (JButton) e.getSource();
		if (buttonPressed.getLocationOnScreen().getX()>1470) {
			if (buttonPressed.getText() == "EAT") {
				ActivableObject objectToEat = (ActivableObject) active_player.getObjects().get(index);
				objectToEat.activate(active_player);
				active_player.getObjects().remove(objectToEat);
				
			}
			for (ActivableObject o : activableObjects) {
				if (o.getPosX() == active_player.getFrontX() && o.getPosY() == active_player.getFrontY()) {
					o.activate(active_player);
				}
			}
		}
		InventoryPanel.getInstance().updateInventory();
		Map.getInstance().requestFocusInWindow();
		updateVisibleButtons();
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
	public HashMap<String, JButton> getButtonsHashMap() {
		return buttons;
	}
	public void setSelectedIndexInventory(int index) {
		this.index = index;
		if(this.index <0) {
			this.index = 0;
		}
		updateVisibleButtons();
	}
	public static ActionPanel getInstance() {
		if (actionPanel_instance == null) {
			actionPanel_instance = new ActionPanel();
		}
		return actionPanel_instance;
	}
}
