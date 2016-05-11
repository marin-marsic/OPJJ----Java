package hr.fer.zemris.java.tecaj_14.formular;

import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Form used for creating a new entry.
 * 
 * @author Marin Maršić
 *
 */
public class FormularEntry {
	
	/**
	 * Entry's title.
	 */
	private String title;
	/**
	 * Entry's text.
	 */
	private String text;
	
	/**
	 * Map for registering the errors which occurred while creating the entry.
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
		this.title = pripremi(req.getParameter("title"));
		this.text = pripremi(req.getParameter("text"));
	}

	/**
	 * Fills the form with the data from the {@link BlogEntry}.
	 * @param entry {@link BlogEntry}.
	 */
	public void popuniIzRecorda(BlogEntry entry) {
		title = entry.getTitle();
		text = entry.getText();
	}

	/**
	 * Fills the given {@link BlogEntry} with informations.
	 * @param entry {@link BlogEntry} to fill.
	 */
	public void popuniURecord(BlogEntry entry) {
		entry.setText(text);
		entry.setTitle(title);
	}

	/**
	 * Finds the errors in the form.
	 */
	public void validiraj() {
		if (title.isEmpty()) {
			greske.put("title", "Naslov je obavezan!");
		}

		if (text.isEmpty()) {
			greske.put("text", "Text je obavezan!");
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
	 * @return Returns the title of the entry.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the entry.
	 * @param title Title of the entry.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the text of the entry.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text of the entry.
	 * @param text Text of the entry.
	 */
	public void setText(String text) {
		this.text = text;
	}

}
