package hr.fer.zemris.java.hw10.jnotepadpp;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Implementation of a {@link JLabel} based component for showing current date
 * and time at the status bar.
 * 
 * @author Marin Maršić
 *
 */
public class TimeComponent extends JLabel {
	private static final long serialVersionUID = 1L;

	private Date currentTime;
	private volatile boolean stopRequested;

	/**
	 * Constructor of a {@link TimeComponent}. Creates new thread for updating
	 * the time.
	 */
	public TimeComponent() {
		
		Thread t = new Thread(() -> {
			while (true) {
				SwingUtilities.invokeLater(() -> {
					currentTime = new Date();
					String time = (currentTime.getYear() + 1900)
							+ "/"
							+ String.format("%02d",
									(currentTime.getMonth() + 1)) + "/"
							+ String.format("%02d", currentTime.getDate())
							+ " " + currentTime.getHours() + ":"
							+ String.format("%02d", currentTime.getMinutes())
							+ ":"
							+ String.format("%02d", currentTime.getSeconds());
					setText(time);
					repaint();
				});
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					if (stopRequested)
						break;
				}
				if (stopRequested)
					break;
			}

		});
		t.setDaemon(true);
		t.start();
		currentTime = new Date();
	}

	/**
	 * Tells the counting thread it is time to stop counting.
	 * 
	 * @param b
	 *            Flag which tells the thread to stop counting if set to true.
	 */
	public void setStopRequested(boolean b) {
		stopRequested = b;

	}
}
