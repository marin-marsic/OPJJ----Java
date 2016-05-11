package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Component representing simple color chooser implementing {@link IColorProvider}.
 * @author Marin Maršić
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Currently selected color.
	 */
	private Color selectedColor;

	/**
	 * List of listeners registered to this object.
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();

	/**
	 * Default constructor for the {@link JColorArea}. Sets the color to white.
	 */
	public JColorArea() {
		selectedColor = Color.WHITE;
		this.addMouseListener(clickedListener);
	}

	/**
	 * Constructor for the {@link JColorArea}. Sets the color to the given one.
	 * @param color Color to set.
	 */
	public JColorArea(Color color) {
		selectedColor = color;
		this.addMouseListener(clickedListener);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	/**
	 * Registers listener to this object.
	 * @param l Listener to be registered.
	 */
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
		l.newColorSelected(this, null, selectedColor);
	}

	/**
	 * Deregisters listener to this object.
	 * @param l Listener to be deregistered.
	 */
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies all the registered listeners.
	 */
	public void notifyListeners() {
		for (ColorChangeListener ccl : listeners) {
			ccl.newColorSelected(this, null, selectedColor);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		int height = this.getPreferredSize().height;
		int width = this.getPreferredSize().width;
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Listener implementing actions needed to be executed on mouse click.
	 */
	private MouseListener clickedListener = new MouseListener() {

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
			JColorArea.this.changeColor();
		}
	};

	/**
	 * Asks user to change the color of the {@link JColorArea}.
	 */
	protected void changeColor() {
		Color color = JColorChooser.showDialog(JColorArea.this.getParent(),
				"Choose color", selectedColor);
		if (color != null) {
			selectedColor = color;
			this.repaint();
			this.notifyListeners();
		}
	}
}
