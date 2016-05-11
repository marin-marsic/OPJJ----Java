package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the collection containing multiple stacks. Each stack can
 * be resolved by it's name. Each stack implements standard methods for
 * operating on stack.
 * 
 * @author Marin Maršić
 *
 */
public class ObjectMultistack {

	/**
	 * HashMap containing multiple stacks. A stack is represented by it's name
	 * and a {@link MultistackEntry} as a linked head of the list representing
	 * stack.
	 */
	Map<String, MultistackEntry> map = new HashMap<>();

	static class MultistackEntry {
		/**
		 * Reference to a next value added to stack.
		 */
		private MultistackEntry next;
		/**
		 * Value in the certain position of the stack.
		 */
		private ValueWrapper value;

		/**
		 * Constructor for the {@link MultistackEntry}.
		 * 
		 * @param next
		 *            Reference to a next value added to stack.
		 * @param value
		 *            Value to be stored.
		 */
		private MultistackEntry(MultistackEntry next, ValueWrapper value) {
			this.next = next;
			this.value = value;
		}

		/**
		 * @return returns Value.
		 */
		private ValueWrapper getValue() {
			return value;
		}
	}

	/**
	 * Pushes the ValueWrapper object to stack resolved by it's name.
	 * 
	 * @param name
	 *            Name of the stack.
	 * @param valueWrapper
	 *            Value to push.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (!map.containsKey(name)) {
			map.put(name, new MultistackEntry(null, valueWrapper));
		} else {
			MultistackEntry temp = map.get(name);
			while (temp.next != null) {
				temp = temp.next;
			}

			temp.next = new MultistackEntry(null, valueWrapper);
		}
	}

	/**
	 * Pops the ValueWrapperobject object from the stack resolved by it's name.
	 * 
	 * @throws EmptyStackException
	 *             if the stack is already empty.
	 * @param name
	 *            Name of the stack.
	 * @return Popped value.
	 */
	public ValueWrapper pop(String name) {
		if (map.containsKey(name)) {
			MultistackEntry temp = map.get(name);
			if (temp.next == null) {
				map.remove(name);
				return temp.getValue();
			}
			while (temp.next != null) {
				if (temp.next.next == null) {
					ValueWrapper value = temp.next.getValue();
					temp.next = null;
					return value;
				} else {
					temp = temp.next;
				}
			}
		}
		throw new EmptyStackException();
	}

	/**
	 * Returns the ValueWrapperobject object from the top of the stack resolved
	 * by it's name.
	 * 
	 * @throws EmptyStackException
	 *             if the stack is already empty.
	 * @param name
	 *            Name of the stack.
	 * @return Value on the top of the value.
	 */
	public ValueWrapper peek(String name) {
		if (map.containsKey(name)) {
			MultistackEntry temp = map.get(name);
			while (temp.next != null) {
				temp = temp.next;
			}
			ValueWrapper value = temp.getValue();
			return value;
		}
		throw new EmptyStackException();
	}

	/**
	 * Finds out if the specified is empty.
	 * 
	 * @param name
	 *            Name of the stack.
	 * @return Returns true if the stack is empty.
	 */
	public boolean isEmpty(String name) {
		return !map.containsKey(name);
	}

}
