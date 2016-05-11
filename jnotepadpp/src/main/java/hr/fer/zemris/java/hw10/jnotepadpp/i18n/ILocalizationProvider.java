package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

/**
 * Interface used to give us translation of a specific key. Objects which are
 * instances of classes that implement this interface will be able to give us
 * the translations for given keys.
 * 
 * @author Marin Maršić
 *
 */
public interface ILocalizationProvider {

	/**
	 * Adds listener to instance of class implementing this interface.
	 * 
	 * @param listener
	 *            {@link ILocalizationListener}.
	 */
	public void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Removes listener from instance of class implementing this interface.
	 * 
	 * @param listener
	 *            {@link ILocalizationListener}.
	 */
	public void removeLocalizationListener(ILocalizationListener listener);

	/**
	 * Returns local translation of the given key.
	 * 
	 * @param string
	 *            Given key.
	 * @return Returns local translation of the given key.
	 */
	public String getString(String string);
}
