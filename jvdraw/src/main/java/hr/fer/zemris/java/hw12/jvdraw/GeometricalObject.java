package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * Class representing drawable object.
 * 
 * @author Marin Maršić
 *
 */
public abstract class GeometricalObject {

	/**
	 * Name of the object.
	 */
	private String name;
	
	/**
	 * Constructor for the {@link GeometricalObject}.
	 * @param name Name of the object.
	 */
	public GeometricalObject(String name){
		this.name = name;
	}
	
	/**
	 * @return Returns name of the object.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Paints object on the drawable component.
	 * @param g
	 */
	public abstract void paint(Graphics g);
	
	/**
	 * Asks user to change the object.
	 * @param frame Program's main JFrame.
	 */
	public abstract void change(JFrame frame);
	
	/**
	 * @return String representation of object.
	 */
	public abstract String getStringRepresentation();
	
	/**
	 * @return Returns minimum value of x-coordinate contained in object.
	 */
	public abstract int minimumX();
	/**
	 * @return Returns minimum value of y-coordinate contained in object.
	 */
	public abstract int minimumY();
	/**
	 * @return Returns maximum value of x-coordinate contained in object.
	 */
	public abstract int maximumX();
	/**
	 * @return Returns maximum value of y-coordinate contained in object.
	 */
	public abstract int maximumY();
	
	/**
	 * Paints object on image, offset is important here.
	 * @param g
	 * @param xOffset Offset to minimal x-coordinate.
	 * @param yOffset Offset to minimal y-coordinate.
	 */
	public abstract void paintOnImage(Graphics g, int xOffset, int yOffset);
}
