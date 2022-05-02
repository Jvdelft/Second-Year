package controller;

import java.io.Serializable;

import model.Game;

public class Mouse implements Serializable{
    private Game game;

    public Mouse(Game game) {
        this.game = game;
    }

	public void mapEvent(int x, int y) {
		synchronized(game) {
			game.sendPlayer(x, y);
		}
	}
}
