package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Apple;
import model.Game;
import model.GameObject;

import java.util.ArrayList;
import java.util.Vector;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContentScroll extends JScrollPane{
	public ContentScroll() {
		this.setPreferredSize(new Dimension(480,540));
		this.setBackground(Color.BLACK);
	}
}
