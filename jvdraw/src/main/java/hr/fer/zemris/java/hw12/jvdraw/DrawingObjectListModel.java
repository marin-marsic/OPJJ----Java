package hr.fer.zemris.java.hw12.jvdraw;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class behaves as the adapter to a {@link DrawingModel} object.
 * 
 * @author Marin Maršić
 *
 */
public class DrawingObjectListModel implements ListModel<String>,
		DrawingModelListener {

	/**
	 * {@link DrawingModel} adapted.
	 */
	private ObjectsModel model;
	/**
	 * Listeners of this list.
	 */
	private List<ListDataListener> listeners;

	/**
	 * Constructor for the {@link DrawingObjectListModel}.
	 * 
	 * @param model
	 *            {@link DrawingModel} adapted.
	 */
	public DrawingObjectListModel(ObjectsModel model) {
		this.model = model;
		listeners = new ArrayList<>();
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public String getElementAt(int index) {
		return model.getObject(index).getName();
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Method which notifies all listeners of this model.
	 */
	public void notifyListeners() {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this,
					ListDataEvent.CONTENTS_CHANGED, 0,
					DrawingObjectListModel.this.getSize()));
		}
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		notifyListeners();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		notifyListeners();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		notifyListeners();
	}

	/**
	 * Change specific object.
	 * 
	 * @param index
	 *            Position of an object in this model.
	 */
	public void change(int index) {
		model.getObject(index).change(null);
	}
}
