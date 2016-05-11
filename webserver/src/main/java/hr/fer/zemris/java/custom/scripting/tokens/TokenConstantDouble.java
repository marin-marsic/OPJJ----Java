package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of double number token.
 * 
 * @author Marin Maršić
 */
public class TokenConstantDouble extends Token{

	/**
	 * Value stored.
	 */
	private double value;
	
	/**
	 * Constructor.
	 * @param value Value of the number.
	 */
	public TokenConstantDouble(double value){
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		return Double.valueOf(value).toString() + " ";
	}
	
	/**
	 * @return Returns stored value.
	 */
	public double getValue() {
		return value;
	}
	
}
