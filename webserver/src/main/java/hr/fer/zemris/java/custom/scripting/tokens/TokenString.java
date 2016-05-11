package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of string token.
 * 
 * @author Marin Maršić
 */
public class TokenString extends Token{

	/**
	 * String text.
	 */
	private String value;
	
	/**
	 * Constructor.
	 * 
	 * @param value Value of the string.
	 */
	public TokenString(String value){
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		String str = value.replace("\\", "\\\\");
		str = str.replace("\"", "\\\"");
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\t", "\\t");
		return "\"" + str + "\"" + " ";
	}
	
	/**
	 * @return Returns stored value.
	 */
	public String getValue() {
		return value;
	}
	
}
