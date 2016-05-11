package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of operator token.
 * 
 * @author Marin Maršić
 */
public class TokenOperator extends Token{

	/**
	 * Operator symbol.
	 */
	private String symbol;
	
	/**
	 * Constructor.
	 * 
	 * @param symbol Symbol of the operator.
	 */
	public TokenOperator(String symbol){
		this.symbol = symbol;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		return symbol + " ";
	}
	
	/**
	 * @return Returns operator.
	 */
	public String getOperator(){
		return symbol;
	}
}
