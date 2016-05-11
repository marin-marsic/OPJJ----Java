package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * A parser for structured document format.
 * 
 * @author Marin Maršić
 *
 */
public class SmartScriptParser {

	/**
	 * Text to parse.
	 */
	private String text;
	/**
	 * Stack.
	 */
	private ObjectStack stack = new ObjectStack();
	/**
	 * Root document of the file to parse.
	 */
	DocumentNode document = new DocumentNode();

	/**
	 * Constructor. Does the parsing when called.
	 * 
	 * @throws SmartScriptParserException if the parsing cannot be done.
	 * @param text
	 *            Text to parse.
	 */
	public SmartScriptParser(String text) throws SmartScriptParserException{
		this.text = text;

		parseDocument(text, stack);
	}

	// Method for parsing the document.
	/**
	 * Method for parsing document.
	 * @param text Text to parse.
	 * @param stack Stack.
	 */
	private void parseDocument(String text, ObjectStack stack) {

		stack.push(document);

		// separates the text by opened tags
		String[] openTagSplit = text.split("\\{\\$"); 

		// separates the text by text and tags
		String[] textTagSeparator = new String[2 * openTagSplit.length]; 

		textTagSeparator[0] = openTagSplit[0];
		int br = 1;

		// separate the text on text and tags
		for (int i = 1; i < openTagSplit.length; i++) {

			String s = "{$" + openTagSplit[i];

			// if the previous text ends with '\' this is not a tag
			if (openTagSplit[i - 1].endsWith("\\")) {
				textTagSeparator[br] = s;
				br++;
			} 
			
			// it is a tag
			else if (s.contains("$}")) {

				String[] separate = s.split("\\$\\}");

				if (separate.length < 1) {
					throw new SmartScriptParserException("Tag empty!");
				} 
				
				// collect all the text after closed tag to one string
				else if (separate.length > 1) {

					for (int j = 2; j < separate.length; j++) {
						separate[1] += "$}" + separate[j];
					}
				}
				
				// add this to string array that separates text and tags
				textTagSeparator[br] = separate[0] + "$}";
				br++;
				if (separate.length == 2) {
					textTagSeparator[br] = separate[1];
					br++;
				}
			} else {
				throw new SmartScriptParserException("Tag not closed!");
			}

		}

		// we separated the tags and tags, now we have to remove escaping from
		// text
		for (int i = 0; i < textTagSeparator.length; i++) {
			if (textTagSeparator[i] != null
					&& !textTagSeparator[i].startsWith("{$")) {
				String str = "";

				for (int j = 0; j < textTagSeparator[i].length(); j++) {
					char c = textTagSeparator[i].charAt(j);
					
					// escaping 
					if (c == '\\') {
						if (j + 1 < textTagSeparator[i].length()
								&& textTagSeparator[i].charAt(j + 1) == '\\') {
							str += "\\ ";
							j++;
						} else if (j + 1 < textTagSeparator[i].length()
								&& textTagSeparator[i].charAt(j + 1) == '{') {
							str += "{ ";
							j++;
						} else {
							str += c;
						}
					} else {
						str += c;
					}
				}
				textTagSeparator[i] = str;
			}
		}

		// start with parsig
		for (int i = 0; textTagSeparator[i] != null; i++) {
			String s = textTagSeparator[i];

			if (!s.startsWith("{$")
					|| (s.startsWith("{$") && i > 0 && textTagSeparator[i - 1]
							.endsWith("\\"))) {

				// this is a text
				Node node = (Node) stack.pop();
				node.addChildNode(new TextNode(s));
				stack.push(node);
			}

			else {
				// this is a tag
				s = s.substring(2, s.length() - 2);
				s = s.trim();
				if (s.contains("\\$\\}") || s.contains("\\{\\$")) {
					throw new SmartScriptParserException(
							"Warrning! Tagsception!");
				}

				// for tag
				if (s.toUpperCase().startsWith("FOR ")) {
					s = s.substring(3);
					s = s.trim();

					if (!Character.isLetter(s.charAt(0))) {
						throw new SmartScriptParserException(
								"Invalid FOR loop! First argument should be a variable.");
					}

					parseFOR(s, stack);

				}

				// echo tag
				else if (s.startsWith("=")) {
					s = s.substring(1);
					s = s.trim();

					parseECHO(s);
				} 
				
				// end tag
				else if (s.equalsIgnoreCase("END")) {
					stack.pop();
					if (stack.size() == 0) {
						throw new SmartScriptParserException(
								"Invalid number of END tags!");
					}
				} 
				
				// unsupported tag
				else {
					throw new SmartScriptParserException("Unsupported tag!");
				}
			}
		}

		// in the end only document node should be on stack
		if (stack.size() != 1) {
			throw new SmartScriptParserException("Invalid number of END tags!");
		}
	}

	/**
	 * Method that gets the document node tree.
	 * @return Returns DocumentNode.
	 */
	public DocumentNode getDocumentNode() {
		return document;
	}

	//method for parsing echo commands
	/**
	 * Method for parsing echo tag.
	 * @param s Echo tag content.
	 */
	private void parseECHO(String s) {
		
		// states of automat:
		// "" - state of transition
		// "VAR" - state of reading a variable
		// "STR" - state of reading a string 
		// "INT" - state of reading an integer number
		// "REAL" - state of reading a double number
		// "FUN" - state of reading a function
		// operator is only one character so it doesn't have it's state
		
		s += " ";
		ArrayBackedIndexedCollection tokenArray = new ArrayBackedIndexedCollection(2);
		String state = "";
		String value = "";
		int tokenNumber = 0;

		// read sign by sign
		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// currently in state of reading variable
			if (state.equalsIgnoreCase("VAR")) {

				if (Character.isLetterOrDigit(c) || c == '_') {
					value += c;
				} else if (Character.isSpaceChar(c)) {
					tokenArray.add(new TokenVariable(value));
					tokenNumber++;
					value = "";
					state = "";
				} else if (c == '\"') {
					tokenArray.add(new TokenVariable(value));
					tokenNumber++;
					value = "";
					state = "STR";
				} else if (c == '@' && Character.isLetter(s.charAt(j + 1))) {
					tokenArray.add(new TokenVariable(value));
					tokenNumber++;
					value = "@";
					state = "FUN";
				} else if (c == '+' || c == '-' || c == '*' || c == '/'
						|| c == '%') {
					tokenArray.add(new TokenVariable(value));
					tokenNumber++;
					tokenArray.add(new TokenOperator(Character.toString(c)));
					tokenNumber++;
					value = "";
					state = "";
				} else {
					throw new SmartScriptParserException("Unsupported symbol!");
				}
			}

			// currently not reading any token
			else if (state.equalsIgnoreCase("")) {
				if (c == '\"') {
					state = "STR";
				} else if (Character.isDigit(c)) {
					state = "INT";
					j--;
				} else if (Character.isLetter(c)) {
					state = "VAR";
					j--;
				} else if (c == '@' && Character.isLetter(s.charAt(j + 1))) {
					state = "FUN";
					value = "@";
				} else if (Character.isSpaceChar(c)) {
					state = "";
				}

				else if (c == '+' || c == '-' || c == '*' || c == '/'
						|| c == '%') {
					tokenArray.add(new TokenOperator(Character.toString(c)));
					tokenNumber++;
					value = "";
					state = "";
				}
			}

			// currently in state of reading string
			else if (state.equalsIgnoreCase("STR")) {
				if (c == '\\') {
					if (s.charAt(j + 1) == 'n') {
						value += '\n';
						j++;
					} else if (s.charAt(j + 1) == '\\') {
						value += '\\';
						j++;
					} else if (s.charAt(j + 1) == '\"') {
						value += '\"';
						j++;
					} else if (s.charAt(j + 1) == 'r') {
						value += '\r';
						j++;
					} else if (s.charAt(j + 1) == 't') {
						value += '\t';
						j++;
					} else {
						value += c;
					}
				} else if (c == '\"') {
					tokenArray.add(new TokenString(value));
					tokenNumber++;
					value = "";
					state = "";
				} else {
					value += c;
				}
			} 
			
			// currently in state of reading an integer number
			else if (state.equalsIgnoreCase("INT")) {

				if (Character.isDigit(c)) {
					value += c;
					if (j == s.length() - 1) {
						tokenArray.add(new TokenConstantInteger(Integer
								.parseInt(value)));
						tokenNumber++;
					}
				} else if (c == '.') {
					value += c;
					state = "REAL";
				} else {
					tokenArray.add(new TokenConstantInteger(Integer
							.parseInt(value)));
					tokenNumber++;
					state = "";
					value = "";
					j--;
				}
			}

			// currently in state of reading double number
			else if (state.equalsIgnoreCase("REAL")) {

				if (Character.isDigit(c)) {
					value += c;
					if (j == s.length() - 1) {
						tokenArray.add(new TokenConstantDouble(Double
								.parseDouble(value)));
						tokenNumber++;
					}
				} else {
					tokenArray.add(new TokenConstantDouble(Double
							.parseDouble(value)));
					tokenNumber++;
					state = "";
					value = "";
					j--;
				}
			} 
			
			// currently in state of reading function
			else if (state.equalsIgnoreCase("FUN")) {

				if (Character.isLetterOrDigit(c) || c == '_') {
					value += c;
					if (j == s.length() - 1) {
						tokenArray.add(new TokenFunction(value));
						tokenNumber++;
					}
				} else {
					tokenArray.add(new TokenFunction(value));
					tokenNumber++;
					state = "";
					value = "";
					j--;
				}
			}
		}

		// add the tokens to EchoNode
		Token[] tokens = new Token[tokenNumber];

		for (int i = 0; i < tokenArray.size(); i++) {
			tokens[i] = (Token) tokenArray.get(i);
		}

		// update the document tree
		Node node = (Node) stack.pop();
		EchoNode echoNode = new EchoNode(tokens);
		node.addChildNode(echoNode);
		stack.push(node);
	}

	//method for parsing echo commands
	/**
	 * Method for parsing for nodes.
	 * @param s For node context.
	 * @param stack Stack.
	 */
	private static void parseFOR(String s, ObjectStack stack) {
		
		// states of automat:
		// "" - state of transition
		// "VAR" - state of reading a variable
		// "STR" - state of reading a string 
		// "INT" - state of reading an integer number
		// "REAL" - state of reading a double number
		
		s += " ";
		Token[] tokens = new Token[4];
		String state = "VAR";
		String value = "";
		int tokenNumber = 0;

		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// currently in state of reading a variable
			if (state.equalsIgnoreCase("VAR")) {

				if (Character.isLetterOrDigit(c) || c == '_') {
					value += c;
					if (j == s.length() - 1) {
						tokens[tokenNumber] = new TokenVariable(value);
						tokenNumber++;
					}
				} else if (Character.isSpaceChar(c)) {
					tokens[tokenNumber] = new TokenVariable(value);
					tokenNumber++;
					value = "";
					state = "";
				} else if (c == '\"') {
					tokens[tokenNumber] = new TokenVariable(value);
					tokenNumber++;
					value = "";
					state = "STR";
				} else {
					throw new SmartScriptParserException("Unsupported symbol!");
				}
			}

			// currently not reading any token
			else if (state.equalsIgnoreCase("")) {
				if (c == '\"') {
					state = "STR";
				} else if (Character.isDigit(c)) {
					state = "INT"; 
					j--;
				} else if (Character.isLetter(c)) {
					state = "VAR";
					j--;
				} else if (Character.isSpaceChar(c)) {
					state = "";
				} else {
					throw new SmartScriptParserException("Unsupported symbol!");
				}
			}

			// currently in state of reading a string
			else if (state.equalsIgnoreCase("STR")) {
				if (c == '\\') {
					if (s.charAt(j + 1) == 'n') {
						value += '\n';
						j++;
					} else if (s.charAt(j + 1) == '\\') {
						value += '\\';
						j++;
					} else if (s.charAt(j + 1) == '\"') {
						value += '\"';
						j++;
					} else if (s.charAt(j + 1) == 'r') {
						value += '\r';
						j++;
					} else if (s.charAt(j + 1) == 't') {
						value += '\t';
						j++;
					} else {
						value += c;
					}
				} else if (c == '\"') {
					tokens[tokenNumber] = new TokenString(value);
					tokenNumber++;
					value = "";
					state = "";
				} else {
					value += c;
				}
			} 
			
			// currently in state of reading an integer number
			else if (state.equalsIgnoreCase("INT")) {

				if (Character.isDigit(c)) {
					value += c;
					if (j == s.length() - 1) {
						tokens[tokenNumber] = new TokenConstantInteger(
								Integer.parseInt(value));
						tokenNumber++;
					}
				} else if (c == '.') {
					value += c;
					state = "REAL";
				} else {
					tokens[tokenNumber] = new TokenConstantInteger(
							Integer.parseInt(value));
					state = "";
					value = "";
					tokenNumber++;
					j--;
				}
			}

			// currently in state of reading a double number
			else if (state.equalsIgnoreCase("REAL")) {

				if (Character.isDigit(c)) {
					value += c;
					if (j == s.length() - 1) {
						tokens[tokenNumber] = new TokenConstantDouble(
								Double.parseDouble(value));
						tokenNumber++;
					}
				} else {
					tokens[tokenNumber] = new TokenConstantDouble(
							Double.parseDouble(value));
					state = "";
					value = "";
					tokenNumber++;
					j--;
				}
			}
		}
		
		// for command only has 3 or 4 tokens
		if (tokenNumber < 3 || tokenNumber > 4) {
			throw new SmartScriptParserException(
					"Invalid number of arguments in FOR loop!");
		}

		Node node = (Node) stack.pop();
		ForLoopNode forLoopNode = new ForLoopNode((TokenVariable) tokens[0],
				tokens[1], tokens[2], tokens[3]);
		node.addChildNode(forLoopNode);
		stack.push(node);
		stack.push(forLoopNode);
	}
}
