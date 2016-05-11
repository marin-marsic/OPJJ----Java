package javaZI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class DrawingPanel extends JComponent implements ModelObserver{

	private static final long serialVersionUID = 1L;
	private Model model;
	
	public DrawingPanel(Model model) {
		this.addMouseListener(mouseListener);
		this.model = model;
	}

	@Override
	public void execute(Model model) {
		this.model = model;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		((Graphics2D) g).setRenderingHint(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		Dimension size = getSize();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size.width-1, size.height-1);
		
		if (model != null){
			for (Circle c : model.getCircles()){
				int x = c.getX();
				int y = c.getY();
				int r = c.getRadius();
				g.setColor(c.getBackground());
				g.fillOval(x - r, y - r, 2 * r, 2 * r);
				g.setColor(c.getForeground());
				g.drawOval(x - r, y - r, 2 * r, 2 * r);
			}
		}
	}
	
	private MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			int x1 = e.getX();
			int y1 = e.getY();
			
			int index = model.najblizi(x1, y1);
			if (index >= 0){
				model.postaviSelektirani(index);
			}

		}
	};

}
