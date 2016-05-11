package servleti;

import java.util.HashMap;
import java.util.Map;

import javaZI.Circle;

import javax.servlet.http.HttpServletRequest;

public class Formular {

	private String naredba;

	Map<String, String> greske = new HashMap<>();

	public String dohvatiPogresku(String ime) {
		return greske.get(ime);
	}

	public boolean imaPogresaka() {
		return !greske.isEmpty();
	}

	public boolean imaPogresku(String ime) {
		return greske.containsKey(ime);
	}

	public void popuniIzHttpRequesta(HttpServletRequest req) {
		this.naredba = pripremi(req.getParameter("naredba"));
	}

	/**
	 * Finds the errors in the form.
	 */
	public void validiraj() {
		if (naredba.isEmpty()) {
			greske.put("naredba", "Naredba je prazna!");
		}

		String[] textSeparated = naredba.split("\\s+");
		String command = textSeparated[0].toUpperCase();

		if (!command.equals("OBRISI")
				&& !(command.equals("DODAJ") && textSeparated.length == 6)
				&& !(command.equals("SELEKTIRAJ") && textSeparated.length == 2)
				&& !(command.equals("DESELEKTIRAJ"))
				){
			greske.put("naredba", "Naredba je neispravna!");
		}

	}

	/**
	 * Sets the given string to an empty string if the given one is null.
	 * 
	 * @param s
	 *            Given string.
	 * @return Returns validated string.
	 */
	private String pripremi(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	public String getNaredba() {
		return naredba;
	}

	public void setNaredba(String naredba) {
		this.naredba = naredba;
	}

}
