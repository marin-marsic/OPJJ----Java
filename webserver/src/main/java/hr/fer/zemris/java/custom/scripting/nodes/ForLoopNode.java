package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * A node representing a single for-loop construct. It inherits from Node class.
 * 
 * @author Marin Maršić
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Variable for iteration.
	 */
	private TokenVariable variable;
	/**
	 * Start value of iteration.
	 */
	private Token startExpression;
	/**
	 * End value of iteration.
	 */
	private Token endExpression;
	/**
	 * Step of iteration.
	 */
	private Token stepExpression; // can be null

	/**
	 * Constructor.
	 * @param variable
	 * @param startExpression 
	 * @param endExpression
	 * @param stepExpression
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		String str = "{$ FOR  ";
		str += (variable.asText() + "  " + startExpression.asText() + "  "
				+ endExpression.asText() + "  ");
		if (stepExpression != null) {
			str += (stepExpression.asText() + " ");
		}
		return str + "$}";
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#writeChildNodes()
	 */
	@Override
	public String childNodesToString() {
		return (super.childNodesToString() + "{$ END $}");
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

	/**
	 * @return Returns variable.
	 */
	public TokenVariable getVariable() {
		return variable;
	}

	/**
	 * @return Returns start expression.
	 */
	public Token getStartExpression() {
		return startExpression;
	}

	/**
	 * @return Returns end expression.
	 */
	public Token getEndExpression() {
		return endExpression;
	}

	/**
	 * @return Returns step expression.
	 */
	public Token getStepExpression() {
		return stepExpression;
	}

	
}
