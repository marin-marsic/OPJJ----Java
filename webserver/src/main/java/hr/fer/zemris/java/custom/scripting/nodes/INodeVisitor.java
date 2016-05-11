package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface containing methods which need to be executed while visiting
 * specific representation of a {@link Node}.
 * 
 * @author Marin Maršić
 *
 */
public interface INodeVisitor {

	/**
	 * Method which executes an action while visiting {@link TextNode} object.
	 * 
	 * @param node
	 *            {@link TextNode} node being visited.
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Method which executes an action while visiting {@link ForLoopNode} object.
	 * 
	 * @param node
	 *            {@link ForLoopNode} node being visited.
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Method which executes an action while visiting {@link EchoNode} object.
	 * 
	 * @param node
	 *            {@link EchoNode} node being visited.
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Method which executes an action while visiting {@link DocumentNode} object.
	 * 
	 * @param node
	 *            {@link DocumentNode} node being visited.
	 */
	public void visitDocumentNode(DocumentNode node);

}
