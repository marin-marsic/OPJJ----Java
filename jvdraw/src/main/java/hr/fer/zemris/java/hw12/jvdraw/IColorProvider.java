package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;

/**
 * Interface implementing some color provider needs to implement.
 * 
 * @author Marin Maršić
 *
 */
public interface IColorProvider {

	/**
	 * @return Returns current color from the provider.
	 */
	public Color getCurrentColor();
}
