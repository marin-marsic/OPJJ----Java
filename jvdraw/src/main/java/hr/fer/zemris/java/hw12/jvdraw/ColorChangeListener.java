package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;

/**
 * Interface implementing method which needs to be executed when color of the
 * {@link IColorProvider} changes.
 * 
 * @author Marin Maršić
 *
 */
public interface ColorChangeListener {

	/**
	 * Method which needs to be executed when color of the
	 * {@link IColorProvider} changes.
	 * 
	 * @param source Observed object.
	 * @param oldColor old color.
	 * @param newColor new color.
	 */
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor);
}
