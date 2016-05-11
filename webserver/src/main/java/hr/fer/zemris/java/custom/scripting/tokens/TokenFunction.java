package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Representation of function token.
 * 
 * @author Marin Maršić
 */
public class TokenFunction extends Token{

	/**
	 * Name of the function.
	 */
	private String name;
	
	/**
	 * Constructor.
	 * @param name Name of the string.
	 */
	public TokenFunction(String name){
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.tokens.Token#asText()
	 */
	@Override
	public String asText() {
		return name + " ";
	}
	
	/**
	 * @return Returns name of the function.
	 */
	public String getName(){
		return name;
	}
		
}
