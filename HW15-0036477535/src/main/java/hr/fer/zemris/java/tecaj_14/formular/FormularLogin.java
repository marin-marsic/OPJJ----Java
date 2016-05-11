package hr.fer.zemris.java.tecaj_14.formular;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Form used for logging in.
 * 
 * @author Marin Maršić
 *
 */
public class FormularLogin {

	/**
	 * Nick of the user.
	 */
	private String nick;
	/**
	 * Password of the user.
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
		this.password = pripremi(req.getParameter("password"));
		this.nick = pripremi(req.getParameter("nick"));
	}

	/**
	 * Finds the errors in the form.
	 */
	public void validiraj() {
		if (nick.isEmpty()) {
			greske.put("nick", "Korisničko ime je obavezno!");
		}

		if (password.isEmpty()) {
			greske.put("password", "Lozinka je obavezna!");
		} else {
			BlogUser newUser = new BlogUser();
			newUser.setPasswordHash(password);
			try {
				BlogUser user = new JPADAOImpl().getUserByNick(nick);
				if (user == null) {
					greske.put("nick", "Nepostojeći korisnik!");
				}
				if (!user.getPasswordHash().equals(newUser.getPasswordHash())) {
					greske.put("password", "Neispravna lozinka!");
				}
			} catch (Exception e) {
				greske.put("nick", "Nepostojeći korisnik!");
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
	 * @return Returns nick.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the nick.
	 * @param nick Nick of the user.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return Gets the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password
	 * @param password User'password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
