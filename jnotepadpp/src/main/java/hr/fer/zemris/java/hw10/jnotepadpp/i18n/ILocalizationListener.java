package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

/**
 * Interface representing listener of {@link LocalizationProvider} object. It
 * takes some actions when language changes.
 * 
 * @author Marin Maršić
 *
 */
public interface ILocalizationListener {

	/**
	 * Action which needs to be done when language changes.
	 */
	public void localizationChanged();
}
