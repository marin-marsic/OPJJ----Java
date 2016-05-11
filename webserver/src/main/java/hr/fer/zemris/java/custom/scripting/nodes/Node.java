package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * Base class for all graph nodes.
 * 
 * @author Marin Maršić
 *
 */
public abstract class Node {

	/**
	 * Collection containing nodes children (subnodes).
	 */
	private ArrayBackedIndexedCollection collection;

	/**
	 * Adds given child to an internally managed collection of children.
	 * 
	 * @param child
	 *            A node to ad as a child.
	 */
	public void addChildNode(Node child) {

		if (collection == null) {
			collection = new ArrayBackedIndexedCollection(2);
		}

		collection.add(child);
	}

	/**
	 * Returns a number of (direct) children.
	 * 
	 * @return returns a number of (direct) children.
	 */
	public int numberOfChildren() {
		return collection.size();
	}

	/**
	 * Returns selected child or throws an IndexOutOfBoundsException if the
	 * index is invalid.
	 * 
	 * @throws IndexOutOfBoundsException
	 * @param index
	 *            Index of a specific child.
	 * @return Returns child.
	 */
	public Node getChild(int index) throws IndexOutOfBoundsException {
		return (Node) collection.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "";
	}

	/**
	 * Returns the string containing all of the nods children.
	 * 
	 * @return Returns the string containing all of the nods children.
	 */
	public String childNodesToString() {
		String str = "";

		if (collection != null) {

			for (int i = 0; i < collection.size(); i++) {

				Node node = (Node) collection.get(i);
				str += (node.toString() + " ");
				str += node.childNodesToString();
			}
		}
		return str;
	}

	/**
	 * Method which calls action which needs to be executed while visiting this
	 * node based on given {@link INodeVisitor} implementation.
	 * 
	 * @param visitor
	 *            Concrete implementation of an {@link INodeVisitor}.
	 */
	public abstract void accept(INodeVisitor visitor);

}
