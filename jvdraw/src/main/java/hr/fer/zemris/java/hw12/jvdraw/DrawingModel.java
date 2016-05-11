package hr.fer.zemris.java.hw12.jvdraw;

/**
 * Interface implementing methods needed to handle a drawing model containing
 * {@link GeometricalObject}.
 * 
 * @author Marin Maršić
 *
 */
public interface DrawingModel {

	/**
	 * @return Returns number of drawable objects contained by this model.
	 */
	public int getSize();

	/**
	 * @param index Position of the object in the model.
	 * @return Returns object from the model on the position given by the index.
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Adds new object to model.
	 * @param object Object to be added to model.
	 */
	public void add(GeometricalObject object);

	/**
	 * Registers new listener on a model.
	 * @param l Listener.
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Deregisters new listener on a model.
	 * @param l Listener.
	 */
	public void removeDrawingModelListener(DrawingModelListener l);

}
