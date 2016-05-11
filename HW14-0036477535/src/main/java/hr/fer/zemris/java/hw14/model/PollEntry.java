package hr.fer.zemris.java.hw14.model;

/**
 * Class representing Polls entries.
 * 
 * @author Marin Maršić
 *
 */
public class PollEntry {
	/**
	 * ID of the poll.
	 */
	private long id;
	/**
	 * Title of the poll.
	 */
	private String title;
	/**
	 * Message of the title.
	 */
	private String message;

	/**
	 * Default constructor for the {@link PollEntry}.
	 */
	public PollEntry() {
	}

	/**
	 * @return Returns ID of the poll.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the ID of the poll.
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Returns title of the poll.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the poll.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the message of the title.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message of the title.
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}