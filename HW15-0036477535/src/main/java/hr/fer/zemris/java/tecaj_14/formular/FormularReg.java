package hr.fer.zemris.java.tecaj_14.formular;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Form used for creating a new user.
 * 
 * @author Marin Maršić
 *
 */
public class FormularReg {

	/**
	 * User's name.
	 */
	private String ime;
	/**
	 * User's surname.
	 */
	private String prezime;
	/**
	 * User's email.
	 */
	private String email;
	/**
	 * User's nick.
	 */
	private String nick;
	/**
	 * User's password.
	 */
	private String password;

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
		this.ime = pripremi(req.getParameter("ime"));
		this.prezime = pripremi(req.getParameter("prezime"));
		this.email = pripremi(req.getParameter("email"));
		this.password = pripremi(req.getParameter("password"));
		this.nick = pripremi(req.getParameter("nick"));
	}

	/**
	 * Fills the form with the data from the {@link BlogUser}.
	 * @param user {@link BlogUser}.
	 */
	public void popuniIzRecorda(BlogUser user) {
		ime = user.getFirstName();
		prezime = user.getLastName();
		nick = user.getNick();
		email = user.getEmail();
	}

	/**
	 * Fills the given {@link BlogUser} with informations.
	 * @param user {@link BlogUser} to fill.
	 */
	public void popuniURecord(BlogUser user) {
		user.setFirstName(ime);
		user.setLastName(prezime);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(password);
	}

	/**
	 * Finds the errors in the form.
	 */
	public void validiraj() {
		greske.clear();

		if (ime.isEmpty()) {
			greske.put("ime", "Ime je obavezno!");
		}

		if (prezime.isEmpty()) {
			greske.put("prezime", "Prezime je obavezno!");
		}

		if (nick.isEmpty()) {
			greske.put("nick", "Nadimak je obavezan!");
		} else {
			BlogUser user = new JPADAOImpl().getUserByNick(nick);
			if (user != null) {
				greske.put("nick", "Nadimak već postoji!");
			}
		}

		if (password.isEmpty()) {
			greske.put("password", "Lozinka je obavezna!");
		}

		if (this.email.isEmpty()) {
			greske.put("email", "EMail je obavezan!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if (l < 3 || p == -1 || p == 0 || p == l - 1) {
				greske.put("email", "EMail nije ispravnog formata.");
			}
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
	 * @return Returns user's name.
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Sets user's name.
	 * @param ime User's name.
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * @return Returns user's surname.
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Sets user's surname.
	 * @param prezime User's surname.
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	/**
	 * @return Returns user's e-mail.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user's e-mail.
	 * @param email User's e-mail.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns user's nick.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets user's nick.
	 * @param nick User's nick.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return Returns user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets user's password.
	 * @param password User's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
