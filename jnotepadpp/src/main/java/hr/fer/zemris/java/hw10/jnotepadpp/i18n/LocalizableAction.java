package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

import javax.swing.AbstractAction;

/**
 * Class which extends {@link AbstractAction} and represents action which is
 * locally translated.
 * 
 * @author Marin Maršić
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private String key;
	private ILocalizationProvider lp;

	/**
	 * Constructor which connects action whit some translatable key.
	 * 
	 * @param key
	 *            Key for translating.
	 * @param lp
	 *            {@link ILocalizationProvider} object.
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		this.putValue(NAME, lp.getString(key));

		this.lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				LocalizableAction.this.putValue(NAME, lp.getString(key));
			}
		});
	}

	/**
	 * @return Returns key connected to the action.
	 */
	public String getKey() {
		return key;
	}

}
