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
    private MapDrawer mapDrawer = MapDrawer.getInstance();
    private Status status = new Status();
    private CardLayout cards = new CardLayout();
    private MainMenu mainMenu = new MainMenu();
    private Sums active_player;
    private static Window WindowInstance;
    private MenuInGame menuInGame;

    private Window(String title) {
    	super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0, 0, 1920, 1020);
        groupPanel.add(mapDrawer, BorderLayout.CENTER);
        groupPanel.add(status, BorderLayout.EAST);
        this.getContentPane().setLayout(cards);
        this.getContentPane().add((JPanel)mainMenu);
        this.getContentPane().add(groupPanel);
        makeMenu();
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
                	//Window.Exit();
                   System.exit(0);
                }
            }
        };
        this.addWindowListener(exitListener);
    }

    public void setGameObjects(ArrayList<GameObject> objects) {
        this.mapDrawer.setObjects(objects);
        this.mapDrawer.redraw();
    }

    public void update() {
        groupPanel.repaint();
    }

    public void setKeyListener(KeyListener keyboard) {
        this.mapDrawer.addKeyListener(keyboard);
    }

    public void setMouseListener(Mouse m) {
        this.mapDrawer.addMouse(m);
    }

	public int getMapSizeW() {
		if (mapDrawer.getCurrentMap() != null) {
			return mapDrawer.getCurrentMap().getSizeW();
		}
		return 0;
	}
	public int getMapSizeH() {
		if (mapDrawer.getCurrentMap() != null) {
			return mapDrawer.getCurrentMap().getSizeH();
		}
		return 0;
	}
	public void escapePressed() {
		if (groupPanel.isVisible()) {
			cards.next(this.getContentPane());
		}
	}
	
	public void setPlayer(Sums o) {
		active_player = o;
		status.setPlayer(o);
	}
	public void makeMenu() {
		ArrayList<JButton> Buttons = mainMenu.getButtons();
		for (int i = 0; i < Buttons.size();i++) {
			Buttons.get(i).addActionListener(this);
		}
	}
	public void makeMenuInGame() {
		ArrayList<JButton> Buttons = menuInGame.getButtons();
		for (int i = 0; i < Buttons.size();i++) {
			Buttons.get(i).addActionListener(this);
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "EXIT") {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if (e.getActionCommand() == "NEW GAME") {
			initGame();
		}
		else if (e.getActionCommand() == "CONTINUE") {
			this.setPlayer(active_player);
			Load load = new Load();
		}
		else if (e.getActionCommand() == "RESUME") {
			cards.previous(this.getContentPane());
			mapDrawer.requestFocusInWindow();
		}
		else if (e.getActionCommand() == "QUIT") {
			Window.Exit();
			System.exit(0);
		}
		else if (e.getActionCommand() == "HELP") {
			
		}
	}
	public static void Exit() {
		Save save = new Save();
	}
	public void initGame() {
		this.setPlayer(active_player);
		Game game = Game.getInstance();
		Keyboard keyboard = Keyboard.getInstance();
        Mouse mouse = new Mouse(game);
        this.setKeyListener(keyboard);
        this.setMouseListener(mouse);
		cards.next(this.getContentPane());
		mapDrawer.requestFocusInWindow();
		menuInGame = new MenuInGame();
        this.getContentPane().add((JPanel)menuInGame);
        makeMenuInGame();
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
	public MapDrawer getMapDrawer() {
		return this.mapDrawer;
	}
	public Sums getActivePlayer() {
		return active_player;
	}
	/*public Window getInstance() {
		if (InstanceWindow == null) {
			InstanceWindow = new Window("Game");
		}
		return InstanceWindow;
	}*/
}
