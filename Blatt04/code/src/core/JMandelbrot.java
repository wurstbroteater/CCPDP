package core;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ui.*;

public class JMandelbrot {
	public static void main(String[] args) {
		JFrame win = new JFrame();
		
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setSize(1920, 1080);
		win.setVisible(true);
		
		DrawPanel panel = new DrawPanel();
		win.getContentPane().setLayout(new BorderLayout());
		win.getContentPane().add(panel, BorderLayout.CENTER);
		
		panel.drawMandelbrot();
		win.validate();
		win.repaint();
	}
}
