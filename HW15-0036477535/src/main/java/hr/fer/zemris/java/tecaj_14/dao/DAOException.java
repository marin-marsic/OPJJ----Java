package hr.fer.zemris.java.tecaj_14.dao;
/**
 * Class representing excepting which may occur during execution of some
 * database queries.
 * 
 * @author Marin Maršić
 *
 */
public class DAOException extends RuntimeException {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for {@link DAOException}.
	 */
	public DAOException() {
	}

	/**
	 * Constructor for {@link DAOException}.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Cause of the exception.
	 * @param enableSuppression
	 *            Flag which tells us if the suppression is enabled.
	 * @param writableStackTrace
	 *            Flag which tells us if the stack trace is writable.
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor for {@link DAOException}.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Cause of the exception.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for {@link DAOException}.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructor for {@link DAOException}.
	 * 
	 * @param cause
	 *            Cause of the exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}

