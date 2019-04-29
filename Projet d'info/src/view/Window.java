package view;

import model.GameObject;
import model.Load;
import model.Save;
import model.Sums;
import model.Constantes;
import model.Game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.io.IOException;

import controller.Keyboard;
import controller.Mouse;

public class Window extends JFrame implements ActionListener {
	private JPanel groupPanel = new JPanel(new BorderLayout());
    private Map map = Map.getInstance();
    private Status status = new Status();
    private CardLayout cards = new CardLayout();
    private MainMenu mainMenu = new MainMenu();
    private Sums active_player;
    private static Window WindowInstance;

    private Window(String title) {
    	super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0, 0, 1920, 1020);
        groupPanel.add(map, BorderLayout.CENTER);
        groupPanel.add(status, BorderLayout.EAST);
        this.getContentPane().setLayout(cards);
        this.getContentPane().add((JPanel) mainMenu);
        this.getContentPane().add(groupPanel);
        MakeMenus();
        //this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                     null, "Are You Sure to Close Application?", 
                     "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                     JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                	Window.Exit();
                   System.exit(0);
                }
            }
        };
        this.addWindowListener(exitListener);
    }

    public void setGameObjects(ArrayList<GameObject> objects) {
        this.map.setObjects(objects);
        this.map.redraw();
    }

    public void update() {
        groupPanel.repaint();
        status.getInventoryPanel().updateInventory();
    }

    public void setKeyListener(KeyListener keyboard) {
        this.map.addKeyListener(keyboard);
    }

    public void setMouseListener(Mouse m) {
        this.map.addMouse(m);
    }

	public int getHMapSize() {
		return map.tileHorizontale;
	}
	public int getVMapSize() {
		return map.tileVerticale;
	}
	
	public void setPlayer(Sums p) {
		active_player = p;
		status.setPlayer(p);
	}
	public void MakeMenus() {
		ArrayList<JButton> Buttons = mainMenu.getButtons();
		for (int i = 0; i < Buttons.size();i++) {
			Buttons.get(i).addActionListener(this);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "EXIT") {
			Window.Exit();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if (e.getActionCommand() == "NEW GAME") {
			this.setPlayer(active_player);
			Game game = Game.getInstance();
			Keyboard keyboard = new Keyboard(game);
	        Mouse mouse = new Mouse(game);
	        Window.getInstance().setKeyListener(keyboard);
	        Window.getInstance().setMouseListener(mouse);
			cards.next(this.getContentPane());
			map.requestFocusInWindow();
		}
		else if (e.getActionCommand() == "CONTINUE") {
			this.setPlayer(active_player);
			Game game = Game.getInstance();
			Keyboard keyboard = new Keyboard(game);
	        Mouse mouse = new Mouse(game);
	        Window.getInstance().setKeyListener(keyboard);
	        Window.getInstance().setMouseListener(mouse);
			Load load = new Load();
			cards.next(this.getContentPane());
			map.requestFocusInWindow();
		}
	}
	public static void Exit() {
		Save save = new Save();
	}
	public static Window getInstance() {
		if (WindowInstance == null) {
			WindowInstance = new Window("El joc de la muerte");
		}
		return WindowInstance;
	}
	public Status getStatus() {
		return this.status;
	}
	public Map getMap() {
		return this.map;
	}
	/*public Window getInstance() {
		if (InstanceWindow == null) {
			InstanceWindow = new Window("Game");
		}
		return InstanceWindow;
	}*/
}
