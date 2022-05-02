package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.Game;
import view.Window;

public class Keyboard implements KeyListener {
    private Game game;
    private static Keyboard keyboard_instance;

    private Keyboard(Game game) {
        this.game = game;
    }
    public static Keyboard getInstance() { 
    	if (keyboard_instance == null) {
    		keyboard_instance = new Keyboard(Game.getInstance());
    	}
    	return keyboard_instance;
    }
    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        switch (key) {
        case KeyEvent.VK_RIGHT:
            game.movePlayer(1, 0,game.getActivePlayer());
            break;
        case KeyEvent.VK_LEFT:
            game.movePlayer(-1, 0, game.getActivePlayer());
            break;
        case KeyEvent.VK_DOWN:
            game.movePlayer(0, 1, game.getActivePlayer());
            break;
        case KeyEvent.VK_UP:
            game.movePlayer(0, -1, game.getActivePlayer());
             break;
         case KeyEvent.VK_SPACE:
             game.action();
             break;
        case KeyEvent.VK_P:
             game.playerPos();
        case KeyEvent.VK_ESCAPE:
        	Window.getInstance().escapePressed();
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
