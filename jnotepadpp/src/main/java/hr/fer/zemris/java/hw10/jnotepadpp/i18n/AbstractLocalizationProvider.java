package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

import java.util.ArrayList;

/**
 * Implements ILocalizationProvider interface and adds it the ability to
 * register, de-register and inform (fire() method) listeners.
 * 
 * @author Marin Maršić
 *
 */
public abstract class AbstractLocalizationProvider implements
		ILocalizationProvider {

	private ArrayList<ILocalizationListener> list = new ArrayList<>();

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		list.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		list.remove(listener);
	}

	/**
	 * Method for informing all the listeners.
	 */
	public void fire() {
		for (ILocalizationListener l : list) {
			l.localizationChanged();
		}
	}
}
