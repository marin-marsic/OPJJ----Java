package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of integer number token.
 * 
 * @author Marin Maršić
 */
public class TokenConstantInteger extends Token{

	/**
	 * Value stored.
	 */
	private int value;
	
	/**
	 * Constructor.
	 * @param value Value of the number.
	 */
	public TokenConstantInteger(int value){
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		return Integer.valueOf(value).toString() + " ";
	}
	
	/**
	 * @return Returns stored value.
	 */
	public int getValue() {
		return value;
	}
}
