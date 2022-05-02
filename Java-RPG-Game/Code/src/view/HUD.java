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
	private int BAR_LENGTH = Constantes.BAR_LENGTH;
	private int BAR_WIDTH = Constantes.BAR_WIDTH;
	private int AVATAR_SIZE = Constantes.AVATAR_SIZE;
	private static HUD HUD_instance;
	private HUD() {
		this.setOpaque(true);
	}
	private void drawBars(Graphics g) {
		int length_ok;
		String name = null;
		Color color = null;
		int maxNeed = 0;
		int need = 0;
		for (int i = 0; i<5;i++) {
			g.setColor(Color.WHITE);
			switch(i) {
			case 0 : g.drawString("Argent ", 0, 150);
					 g.drawString("x" +p.getHouse().getMoney(),35+Constantes.image_size, 90+Constantes.image_size);
					 g.drawImage(Constantes.coin, 50, 110, Constantes.image_size, Constantes.image_size,this);
					 name = "Energy";
					 color = Color.GREEN;
					 maxNeed = p.getMaxEnergy();
					 need = (int) p.getEnergy();
					 break;
			case 1 : name = "Hunger"; color = Color.BLUE; maxNeed = p.getMaxFaim(); need = p.getFaim(); break;
			case 2 : name = "Hygiene"; color = Color.YELLOW;maxNeed = p.getMaxHygiene(); need = p.getHygiene(); break;
			case 3 : name = "Happiness"; color = Color.CYAN;maxNeed = p.getMaxHappiness(); need = p.getHappiness(); break;
			case 4 : name = "Toilet"; color = Color.MAGENTA;maxNeed = p.getMaxToilet(); need = p.getToilet(); break;
			}
	        g.drawString(name, 0, i*50+190);
	        g.setColor(Color.RED);
	        g.fillRect(0, i*50+200, BAR_LENGTH*maxNeed, BAR_WIDTH);
	        g.setColor(color);
	        length_ok = (int) Math.round(BAR_LENGTH*need);
	        g.fillRect(0, i*50+200, length_ok, BAR_WIDTH);
			
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
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (p != null) {
			g.setFont(new Font("Monotype Corsiva", Font.BOLD, 18));
			g.drawImage(Constantes.backgroundStatus, 0, 0, 450, 500, this);
			g.drawImage(((Sums) p).getFaceSprite(), 150, 25, AVATAR_SIZE, AVATAR_SIZE, this);
			drawBars(g);
		}
	}
}
