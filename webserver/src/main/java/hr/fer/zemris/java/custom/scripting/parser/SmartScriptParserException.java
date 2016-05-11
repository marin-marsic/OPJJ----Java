package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception that is thrown if the parsing cannot be done.
 * 
 * @author Marin Maršić
 *
 */
public class SmartScriptParserException extends RuntimeException{

	/**
	 * Constructor.
	 * @param string Error message.
	 */
	public SmartScriptParserException(String string) {
		super(string);
	}

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 1L;

}
