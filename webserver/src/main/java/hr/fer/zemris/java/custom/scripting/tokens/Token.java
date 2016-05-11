package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Base class for all tokens.
 * 
 * @author Marin Maršić
 *
 */
public class Token {

	/**
	 * Returns the string representation of token. For string and numbers it's
	 * their value, and for the variable, operator and function it's their name.
	 * 
	 * @return String representation of token.
	 */
	public String asText() {
		return "";
	}
	
}
