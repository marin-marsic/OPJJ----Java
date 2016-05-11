package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class which extends {@link AbstractLocalizationProvider}. This class is a
 * Singleton.
 * 
 * @author Marin Maršić
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	private static final LocalizationProvider instance = new LocalizationProvider();
	private static String language;
	private static ResourceBundle bundle;

	/**
	 * Private constructor.
	 */
	private LocalizationProvider() {
		language = "en";
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw10.jnotepadpp.i18n.prijevodi", locale);
	}

	@Override
	public String getString(String string) {
		String value = bundle.getString(string);
		return new String(value.getBytes(StandardCharsets.ISO_8859_1),
				StandardCharsets.UTF_8);
	}

	/**
	 * @return Returns instance of this class.
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets language to a given one.
	 * 
	 * @param language
	 *            Language to be set.
	 */
	public void setLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw10.jnotepadpp.i18n.prijevodi", locale);
		fire();
	}
}
