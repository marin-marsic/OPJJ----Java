package hr.fer.zemris.java.tecaj_14.formular;

import hr.fer.zemris.java.tecaj_14.model.BlogComment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Form used for creating a new comment.
 * 
 * @author Marin Maršić
 *
 */
public class FormularComment {

	/**
	 * Message of the comment.
	 */
	private String message;

	/**
	 * Map for registering the errors which occurred while creating the comment.
	 */
	Map<String, String> greske = new HashMap<>();

	/**
	 * Gets the error description by its name.
	 * @param ime Name of the error.
	 * @return Returns description of the error.
	 */
	public String dohvatiPogresku(String ime) {
		return greske.get(ime);
	}

	/**
	 * @return Returns true if there is at least one error.
	 */
	public boolean imaPogresaka() {
		return !greske.isEmpty();
	}

	/**
	 * Finds out if the given error is found in the errors map.
	 * @param ime Name of the error.
	 * @return Returns true if the given error is found in the errors map.
	 */
	public boolean imaPogresku(String ime) {
		return greske.containsKey(ime);
	}

	/**
	 * Fills the form with the data from the {@link HttpServletRequest}.
	 * @param req {@link HttpServletRequest}.
	 */
	public void popuniIzHttpRequesta(HttpServletRequest req) {
		this.message = pripremi(req.getParameter("message"));
	}

	/**
	 * Fills the form with the data from the {@link BlogComment}.
	 * @param comment {@link BlogComment}.
	 */
	public void popuniIzRecorda(BlogComment comment) {
		message = comment.getMessage();
	}

	/**
	 * Fills the given {@link BlogComment} with informations.
	 * @param comment {@link BlogComment} to fill.
	 */
	public void popuniURecord(BlogComment comment) {
		comment.setMessage(message);
	}

	/**
	 * Finds the errors in the form.
	 */
	public void validiraj() {
		if (message.isEmpty()) {
			greske.put("message", "Komentar je prazan!");
		}
	}

	/**
	 * Sets the given string to an empty string if the given one is null.
	 * @param s Given string.
	 * @return Returns validated string.
	 */
	private String pripremi(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * @param message Message of the comment.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
