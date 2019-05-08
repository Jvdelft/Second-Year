package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Constantes;
import model.Sums;

public class HUD extends JPanel{
	private Sums p;
	private int BAR_LENGTH = 1;
	private int BAR_WIDTH = 20;
	private int AVATAR_SIZE = 100;
	private static HUD HUD_instance;
	private HUD() {
		this.setOpaque(true);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (p != null) {
			g.setFont(new Font("Monotype Corsiva", Font.BOLD, 18));
			g.drawImage(Constantes.backgroundStatus, 0, 0, 450, 500, this);
			g.drawImage(((Sums) p).getFaceSprite(), 150, 25, AVATAR_SIZE, AVATAR_SIZE, this);
	        //g.fillRect(150, 25, AVATAR_SIZE, AVATAR_SIZE);
	
			// bars 
	        // Energy 
			g.setColor(Color.WHITE);
			g.drawString("Argent ", 0, 150);
			g.drawString("x" +p.getHouse().getMoney(),35+Constantes.image_size, 90+Constantes.image_size);
			g.drawImage(Constantes.coin, 50, 110, Constantes.image_size, Constantes.image_size,this);
	        g.drawString("Energy", 0, 190);
	        g.setColor(Color.RED);
	        g.fillRect(0, 200, BAR_LENGTH*p.getMaxEnergy(), BAR_WIDTH);
	        g.setColor(Color.GREEN);
	        int length_ok = (int) Math.round(BAR_LENGTH*p.getEnergy());
	        g.fillRect(0, 200, length_ok, BAR_WIDTH);
	        g.setColor(Color.WHITE);
	        g.drawString("Hunger", 0, 240);
	        g.setColor(Color.RED);
	        g.fillRect(0, 250, BAR_LENGTH*p.getMaxFaim(), BAR_WIDTH);
	        g.setColor(Color.BLUE);
	        length_ok = (int) Math.round(BAR_LENGTH*p.getFaim());
	        g.fillRect(0, 250, length_ok, BAR_WIDTH);
	        g.setColor(Color.WHITE);
	        g.drawString("Hygiene", 0, 290);
	        g.setColor(Color.RED);
	        g.fillRect(0, 300, BAR_LENGTH*p.getMaxHygiene()*10, BAR_WIDTH);
	        g.setColor(Color.YELLOW);
	        length_ok = (int) Math.round(BAR_LENGTH*p.getHygiene()*10);
	        g.fillRect(0, 300, length_ok, BAR_WIDTH);
	        g.setColor(Color.WHITE);
	        g.drawString("Happiness", 0, 340);
	        g.setColor(Color.RED);
	        g.fillRect(0, 350, BAR_LENGTH*p.getMaxHappiness(), BAR_WIDTH);
	        g.setColor(Color.CYAN);
	        length_ok = (int) Math.round(BAR_LENGTH*p.getHappiness());
	        g.fillRect(0, 350, length_ok, BAR_WIDTH);
	        g.setColor(Color.WHITE);
	        g.drawString("Toilet", 0, 390);
	        g.setColor(Color.MAGENTA);
	        g.fillRect(0, 400, BAR_LENGTH*p.getMaxToilet(), BAR_WIDTH);
	        g.setColor(Color.RED);
	        length_ok = (int) Math.round(BAR_LENGTH*p.getToilet());
	        g.fillRect(0, 400, length_ok, BAR_WIDTH);
		}
		
	}
	public void setPlayer(Sums p2) {
		this.p = p2;
	}
	public static HUD getInstance() {
		if (HUD_instance == null) {
			HUD_instance = new HUD();
		}
		return HUD_instance;
	}
}
