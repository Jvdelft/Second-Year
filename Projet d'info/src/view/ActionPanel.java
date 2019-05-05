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
		initButton(new JButton("EAT IT"));
		initButton(new JButton("GIVE FLOWER"));
		initButton(new JButton("MAKE LOVE"));
		initButton(new JButton("PLAY TOY"));
		initButton(new JButton("SMOKE"));
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
    	ArrayList <String> typeList = new ArrayList <String>();
    	if (activableObjects != null) {
    	for (ActivableObject object : activableObjects) {
            if (object.isAtPosition(active_player.getFrontX(), active_player.getFrontY())) { 
                if (object.getUser() == active_player.getAgeRange() || object.getUser() == "All") { // = Si l'objet peut etre utilisé par active_player
                	if (object instanceof Sums && ((Sums)object).getAffection(active_player) >= 40) { // = si affection du sums pour active_player est grande
                		typeList.add(((Sums)object).getTypeAffection());
                	}
                	else { typeList.add(object.getType());}
                }
            }
        }
    	if (active_player.getObjects().size() >0 && active_player.getObjects().size() > index) {
    		ActivableObject object = ((ActivableObject) active_player.getObjects().get(index));
    		if (object.getUser() == active_player.getAgeRange() || object.getUser() == "All") {
    			typeList.add(object.getType());
    		}
		}
    	showButtons(typeList);
    	}
    }
	
	public void showButtons(ArrayList<String> typeList) {
		ArrayList<JButton> mustAddButtons = new ArrayList<JButton>();
		for (String type : typeList) {
			mustAddButtons.add((JButton) buttons.get(type));
		}
		for (JButton object : allButtons) {
			if (!(mustAddButtons.contains(object))) {
				removeButton(object);
			}
		}
		if (!(mustAddButtons.isEmpty())){
			for (JButton type : mustAddButtons) {
				this.addButton(type.getText());
			}
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
	}
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();
		if (buttonPressed.isValid() && buttonPressed.getLocationOnScreen().getX()>1470) {
			Game.getInstance().buttonPressed(buttonPressed.getText());
		}
	}
	
	public void setPlayer(Sums s) {
		active_player = s;
	}
	
	public void removeButton(JButton button) {
		if (visibleButtons.contains(button)) {
			this.remove(button);
			visibleButtons.remove(button);
		}
		
	}
	public void addButton(String s) {
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
		else if (!(visibleButtons.isEmpty()) && visibleButtons.get(0) != buttons.get("EAT")){
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
	public void setSelectedIndexInventory(int index) {
		this.index = index;
		if(this.index <0) {
			this.index = 0;
		}
		updateVisibleButtons();
		Game.getInstance().setIndexInventory(index);
	}
	public static ActionPanel getInstance() {
		if (actionPanel_instance == null) {
			actionPanel_instance = new ActionPanel();
		}
		return actionPanel_instance;
	}
}
