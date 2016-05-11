package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

/**
 * This class is a decorator for some other IlocalizationProvider. This class
 * offers two additional methods: connect() and disconnect(), and it manages a
 * connection status.
 * 
 * @author Marin Maršić
 *
 */
public class LocalizationProviderBridge implements ILocalizationProvider {

	private boolean connected = false;
	private LocalizationProvider provider;
	private ILocalizationListener listener;

	/**
	 * Constructor. Stores the given {@link LocalizationProvider} object.
	 * 
	 * @param provider
	 *            {@link LocalizationProvider} object.
	 */
	public LocalizationProviderBridge(LocalizationProvider provider) {
		this.provider = provider;
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		provider.addLocalizationListener(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		provider.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String string) {
		return provider.getString(string);
	}

	/**
	 * This method will deregister registered {@link ILocalizationListener}
	 * object.
	 */
	public void disconnect() {
		connected = false;
		provider = null;
	}

	/**
	 * This method will register an instance of anonimous
	 * {@link ILocalizationListener} on the decorated object.
	 */
	public void connect() {
		if (connected) {
			return;
		}

		connected = true;
		listener = new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				provider.fire();
			}
		};
	}

}
