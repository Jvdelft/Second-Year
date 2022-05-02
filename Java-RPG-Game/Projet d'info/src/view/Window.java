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

import javax.management.timer.Timer;
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
    private static Window windowInstance;
    private MenuInGame menuInGame;

    private Window(String title) {
    	super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0, 0, 1920, 1020);
        groupPanel.add(mapDrawer, BorderLayout.CENTER);
        groupPanel.add(status, BorderLayout.EAST);				//Les panels sont organisés dans un cardLayout permettant de passer aisément d'un panel ou groupe de panel à un autre.
        this.getContentPane().setLayout(cards);
        this.getContentPane().add((JPanel)mainMenu);
        this.getContentPane().add(groupPanel);
        this.setDefaultCloseOperation(Window.DO_NOTHING_ON_CLOSE);
        makeMenu();
        this.setVisible(true);
        WindowListener exitListener = new WindowAdapter() {			//On ajoute une demande de confirmation avant la fermeture de la fenètre.
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                     null, "Are You Sure to Close Application?", 
                     "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                     JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                	System.exit(0);
                }
            }
        };
        this.addWindowListener(exitListener);
    }
    public void update() {
        groupPanel.repaint();
    }
	public void escapePressed() {			//On passe au menuInGame si le joueur presse escape.
		if (groupPanel.isVisible()) {
			cards.next(this.getContentPane());
		}
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
	public static void Exit() {
		Save save = new Save();		//Le jeu est sauvegardé.
		System.exit(0);
	}
	public void initGame() {				//Le jeu est crée et instancié.
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
		if (windowInstance == null) {
			windowInstance = new Window("El joc de la muerte");
		}
		return windowInstance;
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
	public CardLayout getCardLayout() {
		return cards;
	}
	public void setGameObjects(ArrayList<GameObject> objects) {
        this.mapDrawer.setObjects(objects);
        this.mapDrawer.redraw();
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
	public void setPlayer(Sums o) {
		active_player = o;
		status.setPlayer(o);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "EXIT") {
			System.exit(0);
		}
		else if (e.getActionCommand() == "NEW GAME") {
			initGame();
		}
		else if (e.getActionCommand() == "CONTINUE") {		//on charge la dernière sauvegarde.
			this.setPlayer(active_player);
			Load load = new Load();
		}
		else if (e.getActionCommand() == "RESUME") {		//On repasse au panel précédent qui est le jeu.
			cards.previous(this.getContentPane());
			mapDrawer.requestFocusInWindow();
		}
		else if (e.getActionCommand() == "QUIT") {
			System.exit(0);
		}
		else if (e.getActionCommand() == "SAVE AND QUIT") {
			Window.Exit();
		}
		else if (e.getActionCommand() == "HELP") {
			HelpPanel help = new HelpPanel();
			((JButton) help.getButton()).addActionListener(this);	//On crée un panel qui va être ajouté au cardLayout.
			this.getContentPane().add(help);
			cards.next(this.getContentPane());
		}
		else if (e.getActionCommand() == "BACK") {
			cards.previous(this.getContentPane());
		}
	}
}
