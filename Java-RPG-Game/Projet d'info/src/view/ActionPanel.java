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

import controller.ActionPanelListener;
import model.ActivableObject;
import model.Adult;
import model.Game;
import model.GameObject;
import model.Sums;

public class ActionPanel extends JPanel {
	private GridBagLayout box = new GridBagLayout();
	private GridBagConstraints limits = new GridBagConstraints();
	private int x;
	private int y;
	private transient Image background = Toolkit.getDefaultToolkit().createImage("Images/background.jpg");
	private Font font = new Font("Monotype Corsiva", Font.BOLD, 20);
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();
	private ArrayList<JButton> allButtons = new ArrayList<JButton>();
	private ArrayList<JButton> visibleButtons = new ArrayList<JButton>();
	private static ActionPanel actionPanel_instance;
	private transient ActionPanelListener actionpanelListener = new ActionPanelListener();
	private ActionPanel() {
		limits.weightx = 1;
		limits.weighty = 1;
		initButton(new JButton("STOCK"));
		initButton(new JButton("GIVE"));
		initButton(new JButton("TAKE"));
		initButton(new JButton("CLOSE"));
		initButton(new JButton("EAT IT"));
		initButton(new JButton("GIVE FLOWER"));
		initButton(new JButton("MAKE LOVE"));
		initButton(new JButton("SMOKE"));
		initButton(new JButton("BUY"));
		initButton(new JButton("GO TO WORK"));
		this.setLayout(this.box);
		
	}
	public void initButton(JButton button) {
		button.setFont(font);
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLUE);
		buttons.put(button.getText(), button);
		allButtons.add(button);
		button.addActionListener(actionpanelListener);
	}
	public void showButtons(ArrayList<String> typeList) {				//La fonction affiche les bouttons utilisables � cet instant.
		ArrayList<JButton> mustAddButtons = new ArrayList<JButton>();
		for (String type : typeList) {
			mustAddButtons.add((JButton) buttons.get(type));
		}
		for (JButton object : allButtons) {					//On supprime les bouttons qui ne sont plus disponibles
			if (!(mustAddButtons.contains(object))) {
				removeButton(object);
			}
		}
		if (!(mustAddButtons.isEmpty())){					//On ajoute les nouveaux bouttons.
			for (JButton type : mustAddButtons) {
				if (type != null) {
					this.addButton(type.getText());
				}
			}
		}
	}
	
	public void removeButton(JButton button) {
		if (visibleButtons.contains(button)) {
			this.remove(button);
			visibleButtons.remove(button);
		}
		
	}
	public void addButton(String s) {					//Les bouttons sont organis�s en grille et s'ajoute donc l'un � la suite de l'autre dans la grille.
		JButton button = (JButton) buttons.get(s);
		Boolean notIn = true;
		if (visibleButtons.contains(button)) {
			notIn = false;
		}
		x = visibleButtons.size()%3;
		y = visibleButtons.size()/3;
		if (y < 3 && notIn && button != null) {
			limits.gridx = x;
			limits.gridy = y;
			box.setConstraints(button, limits);
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			button.setAlignmentY(Component.CENTER_ALIGNMENT);
			this.add(button);
			visibleButtons.add(button);
		}
	}
	public HashMap<String, JButton> getButtonsHashMap() {
		return buttons;
	}
	public String getFirstVisibleButton() {
		String res = null;
		if (visibleButtons.contains((JButton) buttons.get("INTERACT"))) {
			res = "INTERACT";
		}
		else if (visibleButtons.contains((JButton) buttons.get("OPEN"))) {
			res = "OPEN";
		}
		else if (!(visibleButtons.isEmpty()) && (visibleButtons.get(0) != buttons.get("EAT") && visibleButtons.get(0) != buttons.get("GO TO WORK"))){
			res = visibleButtons.get(0).getText();
		}
		else if (visibleButtons.size()-1>0){
			res = visibleButtons.get(1).getText();
		}
		else if (!(visibleButtons.isEmpty())){
			res = visibleButtons.get(0).getText();
		}
		return res;
	}
	public void setPlayer(Sums s) {					//On change le personnage controll� et donc on retire tous les bouttons car ils ne correspondent plus au nouveau personnage.
		visibleButtons.removeAll(visibleButtons);
		this.removeAll();
	}
	public static ActionPanel getInstance() {
		if (actionPanel_instance == null) {
			actionPanel_instance = new ActionPanel();
		}
		return actionPanel_instance;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
	}
}
