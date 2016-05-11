package hr.fer.zemris.java.hw12.jvdraw;

/**
 * Interface implementing methods needed to handle notifications from observed
 * model.
 * 
 * @author Marin Maršić
 *
 */
public interface DrawingModelListener {

	/**
	 * Method to execute when new object is added to observed model.
	 * @param source Observed {@link DrawingModel}.
	 * @param index0 Index of first changed object.
	 * @param index1 Index of last changed object.
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Method to execute when object is removed from observed model.
	 * @param source Observed {@link DrawingModel}.
	 * @param index0 Index of first changed object.
	 * @param index1 Index of last changed object.
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Method to execute when object from observed model is changed.
	 * @param source Observed {@link DrawingModel}.
	 * @param index0 Index of first changed object.
	 * @param index1 Index of last changed object.
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);

}
