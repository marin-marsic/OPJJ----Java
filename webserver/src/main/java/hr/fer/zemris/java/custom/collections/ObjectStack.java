package hr.fer.zemris.java.custom.collections;

/**
 * An implementation of the stack collection.
 * 
 * @author Marin Maršić
 *
 */
public class ObjectStack {

	private ArrayBackedIndexedCollection stack = new ArrayBackedIndexedCollection(
			2);

	/**
	 * Finds out if the stack is empty.
	 * 
	 * @return Returns true if the stack is empty.
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Gives us the number of currently stored objects on stack.
	 * 
	 * @return
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Pushes given value on the stack. null value is not allowed.
	 * 
	 * @param value
	 *            Value to put on stack.
	 */
	public void push(Object value) {
		stack.add(value);
	}

	/**
	 * Removes last value pushed on stack from stack and returns it. If the
	 * stack is empty when the method pop is called, the method throws
	 * EmptyStackException.
	 * 
	 * @throws EmptyStackException
	 * @return Returns the object last pushed on stack.
	 */
	public Object pop() throws EmptyStackException{

		if (stack.size() == 0) {
			throw new EmptyStackException();
		} else {
			Object value = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			return value;
		}
	}


	/**
	 * Returns last element placed on stack but does not delete it from stack. If the
	 * stack is empty when the method pop is called, the method throws
	 * EmptyStackException.
	 * 
	 * @return Returns the object last pushed on stack.
	 * @throws EmptyStackException
	 */
	public Object peek() throws EmptyStackException{

		if (stack.size() == 0) {
			throw new EmptyStackException();
		} else {
			return stack.get(stack.size() - 1);
		}
	}

	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		stack.clear();
	}
}
