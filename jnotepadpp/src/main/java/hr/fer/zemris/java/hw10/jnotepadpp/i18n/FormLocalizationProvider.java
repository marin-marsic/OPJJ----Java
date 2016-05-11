package hr.fer.zemris.java.hw10.jnotepadpp.i18n;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * FormLocalizationProvider is a class derived from LocalizationProviderBridge.
 * In its constructor it registers itself as a WindowListener to it's JFrame;
 * when frame is opened, it calls connect and when frame is closed, it calls
 * disconnect.
 * 
 * @author Marin Maršić
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	private JFrame frame;
	private WindowListener windowListener;

	/**
	 * Constructor which registers itself as a WindowListener to it's JFrame.
	 * 
	 * @param provider {@link LocalizationProvider} object.
	 * @param frame {@link JFrame} frame.
	 */
	public FormLocalizationProvider(LocalizationProvider provider, JFrame frame) {
		super(provider);
		this.frame = frame;

		windowListener = new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				FormLocalizationProvider.this.disconnect();
				frame.dispose();
			}

			public void windowOpened(WindowEvent e) {
				FormLocalizationProvider.this.disconnect();
			}
		};
	}
}
