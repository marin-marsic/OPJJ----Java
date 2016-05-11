package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * Class representing canvas to be drawn on.
 * @author Marin Maršić
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * {@link DrawingModel} containing objects to be drawn.
	 */
	private DrawingModel source;
	/**
	 * Type of the selected object to be drawn.
	 */
	private String geometricalType;
	/**
	 * Background color.
	 */
	private Color background;
	/**
	 * Foreground color.
	 */
	private Color foreground;
	/**
	 * Object to be drawn while the mouse moves.
	 */
	private GeometricalObject temporaryObject;
	/**
	 * Flag indicating first of two clicks occured.
	 */
	private boolean firstClick = false;
	/**
	 * Saved X-coordinate.
	 */
	private int x1; 
	/**
	 * Saved y-coordinate.
	 */
	private int y1;
		
	/**
	 * Constructor for the {@link JDrawingCanvas}.
	 * @param source {@link DrawingModel} containing objects to be drawn.
	 * @param geometricalType Type of the selected object to be drawn.
	 */
	public JDrawingCanvas(DrawingModel source, String geometricalType) {
		super();
		this.source = source;
		this.geometricalType = geometricalType;
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseMotionListener);
	}

	/**
	 * Sets the selected type of the {@link GeometricalObject} to be drawn.
	 * @param geometricalType Type of the {@link GeometricalObject} to be drawn.
	 */
	public void setGeometricalType(String geometricalType) {
		this.geometricalType = geometricalType;
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		this.source = source;
		this.repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		this.source = source;;
		this.repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		this.source = source;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		((Graphics2D) g).setRenderingHint(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		Dimension size = getSize();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size.width-1, size.height-1);
		
		if (source != null){
			for (int i = 0; i < source.getSize(); i++){
				source.getObject(i).paint(g);
			}
			if (temporaryObject != null && firstClick){
				temporaryObject.paint(g);
			}
		}
	}

	/**
	 * Listener implementing action needed to be executed when mouse click over the canvas.
	 */
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
			if (!firstClick){
				firstClick = true;
				x1 = e.getX();
				y1 = e.getY();
			}
			else{
				if (geometricalType.equals("Line")){
					source.add(new Line(x1, y1, e.getX(), e.getY(), foreground));
				}
				else if (geometricalType.equals("Circle")){
					int x = Math.abs(x1-e.getX());
					int y = Math.abs(y1-e.getY());
					int r = (int) Math.sqrt(x*x + y*y);
					source.add(new Circle(x1, y1, r, foreground));
				}
				else if (geometricalType.equals("Filled circle")){
					int x = Math.abs(x1-e.getX());
					int y = Math.abs(y1-e.getY());
					int r = (int) Math.sqrt(x*x + y*y);
					source.add(new FilledCircle(x1, y1, r, foreground, background));
				}
				firstClick = false;
			}
		}
	};
	
	/**
	 * Listener implementing action needed to be executed when mouse moves over the canvas.
	 */
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			if (firstClick){
				
				if (geometricalType.equals("Line")){
					Line line = new Line(x1, y1, e.getX(), e.getY(), foreground);
					temporaryObject = line;
					repaint();
					line.decrement();
				}
				else if (geometricalType.equals("Circle")){
					int x = Math.abs(x1-e.getX());
					int y = Math.abs(y1-e.getY());
					int r = (int) Math.sqrt(x*x + y*y);
					Circle circle = new Circle(x1, y1, r, foreground);
					temporaryObject = circle;
					repaint();
					circle.decrement();
				}
				else if (geometricalType.equals("Filled circle")){
					int x = Math.abs(x1-e.getX());
					int y = Math.abs(y1-e.getY());
					int r = (int) Math.sqrt(x*x + y*y);
					FilledCircle circle = new FilledCircle(x1, y1, r, foreground, background);
					temporaryObject = circle;
					repaint();
					circle.decrement();
				}
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		}
	};

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor) {
		String name = ((JComponent) source).getName();
		if (name.equals("background")) {
			background = newColor;
		} else if (name.equals("foreground")) {
			foreground = newColor;
		}
	}
}
