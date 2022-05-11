package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class DrawPanel extends JPanel {
	public DrawPanel() {
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,  0), "zoomIn");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoomOut");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,  0), "panLeft");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "panRight");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,    0), "panUp");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,  0), "panDown");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B,  0), "bench");

		getActionMap().put("zoomIn", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom /= 1.1;
				drawMandelbrot();
			}
		});

		getActionMap().put("zoomOut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom *= 1.1;
				drawMandelbrot();
			}
		});

		getActionMap().put("panLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pos_x -= zoom * 40;
				drawMandelbrot();
			}
		});
		
		getActionMap().put("panRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pos_x += zoom * 40;
				drawMandelbrot();
			}
		});
		
		getActionMap().put("panUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pos_y -= zoom * 40;
				drawMandelbrot();
			}
		});
		
		getActionMap().put("panDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pos_y += zoom * 40;
				drawMandelbrot();
			}
		});
		
		getActionMap().put("bench", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				benchmark();
			}
		});

		img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
	}
	
	@Override
	public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

	public void benchmark() {
		for (int i = 0; i < 20; ++i) {
			long start_time = System.nanoTime();
			drawMandelbrot();
			long end_time = System.nanoTime();
			System.out.println("Bench: " + (end_time-start_time)/1000000.0 + "ms");
		}
	}
	
	public void drawMandelbrot() {
		int[] d = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		for(int x = 0; x < 1920; ++x)
			for(int y = 0; y < 1080; ++y) {
				int m = calcMandelbrot(pos_x + zoom*x, pos_y + zoom*y);
				d[1920 * y + x] = colorMap[m];
			}
		repaint();
	}
	
	public int calcMandelbrot(double reC, double imC) {
		double re = 0;
		double im = 0;
		for (int i = 0; i < 360; ++i) {
			double _x = re*re - im*im;
			im = 2*re*im + imC;
			re = _x + reC;
			
			if (re*re + im*im > 16)
				return i;
		}
		return 360;
	}
	
    /**
     * Creates the colorMap array which contains RGB colors as integers
     */
    private int[] initColorMap()
    {
    	int[] colorMap = new int[361];
    	for (int i = 0; i < 360; ++i) {
    		Color c = Color.getHSBColor(0.7f + 1.f/360 * i, 1.0f, 1.0f);
    		colorMap[i] = 
                    (c.getRed() << 16) |
                    (c.getGreen() <<  8) |
                    (c.getBlue() <<  0);
    	}
    	colorMap[360] = 0;
    	return colorMap;
    }
	
    private int[] colorMap = initColorMap();
    
	private double pos_x = -2.5;
	private double pos_y = -1;
	private double zoom = 3.5/1280.0;
	
	private BufferedImage img = null;
}
