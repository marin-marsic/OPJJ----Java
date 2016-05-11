package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * A node representing a command which generates some textual output
 * dynamically. It inherits from Node class.
 * 
 * @author Marin
 *
 */
public class EchoNode extends Node {

	/**
	 * Tokens contained in echo tag.
	 */
	private Token[] tokens;

	/**
	 * Constructor.
	 * @param tokens Tokens contained in echo tag.
	 */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		String str = "{$= ";
		for (int i = 0; i < tokens.length; i++) {
			str += (tokens[i].asText() + " ");
		}
		return str + "$}";
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
	/**
	 * Returns Token stored at given position.
	 * @param index Position of a token.
	 * @return Returns Token stored at given index.
	 */
	public Token tokenAt(int index){
		if (index < 0 || index > tokens.length){
			throw new IndexOutOfBoundsException("Index: " + index);
		}
		return tokens[index];
	}
	
	/**
	 * @return Returns number of stored tokens.
	 */
	public int numberOfTokens(){
		return tokens.length;
	}
}
