package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of variable token.
 * 
 * @author Marin Maršić
 */
public class TokenVariable extends Token{

	/**
	 * Name of the variable.
	 */
	private String name;
	
	/**
	 * Constructor.
	 * 
	 * @param name Name of the variable.
	 */
	public TokenVariable(String name){
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		return name.toString() + " ";
	}

	/**
	 * @return Returns name of the variable.
	 */
	public String getName() {
		return name;
	}
	
	
}
