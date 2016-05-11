package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Class representing label showing rgb values of selected colors.
 * @author Marin Maršić
 *
 */
public class SelectedColorsLabel extends JLabel implements ColorChangeListener {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Background color.
	 */
	private Color background;
	/**
	 * Foreground color.
	 */
	private Color foreground;

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor) {

		String name = ((JComponent) source).getName();
		if (name.equals("background")) {
			background = newColor;
		} else if (name.equals("foreground")) {
			foreground = newColor;
		}

		StringBuilder strb = new StringBuilder();
		if (foreground != null){
			strb.append("Foreground color: (");
			strb.append(foreground.getRed() + ", ");
			strb.append(foreground.getGreen() + ", ");
			strb.append(foreground.getBlue() + "), ");
		}
		
		if (background != null) {
			strb.append("Background color: (");
			strb.append(background.getRed() + ", ");
			strb.append(background.getGreen() + ", ");
			strb.append(background.getBlue());
		}
		
		this.setText(strb.toString());
	}
}
