package hr.fer.zemris.java.hw12.jvdraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representation model containing drawable objects.
 * 
 * @author Marin Maršić
 *
 */
public class ObjectsModel implements DrawingModel {

	/**
	 * List of drawable objects contained by model.
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	/**
	 * Listeners registered to this model.
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);

		for (DrawingModelListener dml : listeners) {
			dml.objectsAdded(this, objects.size() - 1, objects.size() - 1);
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	/**
	 * @return Returns list of stored drawable objects.
	 */
	public List<GeometricalObject> getObjects() {
		return objects;
	}

	/**
	 * Clears the list of stored drawable objects.
	 */
	public void removeAllObjects() {
		objects = new ArrayList<>();
	}

	/**
	 * @return Returns minimum x-coordinate drawn on the picture.
	 */
	public int minimumX() {
		Integer x = null;
		for (GeometricalObject go : objects) {
			if (x == null) {
				x = go.minimumX();
			} else {
				x = Math.min(x, go.minimumX());
			}
		}

		return x;
	}

	/**
	 * @return Returns minimum y-coordinate drawn on the picture.
	 */
	public int minimumY() {
		Integer y = null;
		for (GeometricalObject go : objects) {
			if (y == null) {
				y = go.minimumY();
			} else {
				y = Math.min(y, go.minimumY());
			}
		}

		return y;
	}

	/**
	 * @return Returns maximum x-coordinate drawn on the picture.
	 */
	public int maximumX() {
		Integer x = null;
		for (GeometricalObject go : objects) {
			if (x == null) {
				x = go.maximumX();
			} else {
				x = Math.max(x, go.maximumX());
			}
		}

		return x;
	}

	/**
	 * @return Returns maximum y-coordinate drawn on the picture.
	 */
	public int maximumY() {
		Integer y = null;
		for (GeometricalObject go : objects) {
			if (y == null) {
				y = go.maximumY();
			} else {
				y = Math.max(y, go.maximumY());
			}
		}

		return y;
	}
}
