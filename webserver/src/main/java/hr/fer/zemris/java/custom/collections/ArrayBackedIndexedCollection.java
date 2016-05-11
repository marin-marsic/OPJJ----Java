package hr.fer.zemris.java.custom.collections;

/**
 * Class represents implementation of resizable array-backed collection of
 * objects. It supports duplicated values, but not null values.
 * 
 * @author Marin Maršić
 * @version 1.0
 *
 */
public class ArrayBackedIndexedCollection {

	/**
	 * Current size of collection (number of elements actually stored).
	 */
	private int size;

	/**
	 * Current capacity of allocated array of object references.
	 */
	private int capacity;

	/**
	 * An array of object references which length is determined by capacity
	 * variable.
	 */
	Object[] elements;

	/**
	 * Constructor for ArrayBackedIndexedCollection. Creates array with desired
	 * capacity. Throws IllegalArgumentException if the initial capacity is not
	 * positive integer.
	 * 
	 * @throws IllegalArgumentException
	 * @param initialCapacity
	 *            Capacity of array to create. Must be an integer;
	 */
	public ArrayBackedIndexedCollection(int initialCapacity)
			throws IllegalArgumentException {

		if (initialCapacity < 1) {
			throw new IllegalArgumentException(
					"Initial capacity must be an integer.");
		}
		capacity = initialCapacity;
		elements = new Object[initialCapacity];
		size = 0;
	}

	/**
	 * Default constructor for ArrayBackedIndexedCollection. Creates array with
	 * the capacity of 16.
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}

	/**
	 * Finds out if the collection is empty.
	 * 
	 * @return Returns true if collection contains no objects and false
	 *         otherwise.
	 */
	public boolean isEmpty() {

		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gives us the number of currently stored objects in collection.
	 * 
	 * @return Returns current size of collection.
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds the given object into the collection. Object is added into first
	 * empty place in the elements array. Throws IllegalArgumentException if the
	 * value is null.
	 * 
	 * @throws IllegalArgumentException
	 * @param value
	 *            Object we want to add to collection.
	 */
	public void add(Object value) throws IllegalArgumentException {

		if (value.equals(null)) {
			throw new IllegalArgumentException(
					"\'null\' cannot be storedin the collection.");
		}

		if (size >= capacity) {

			Object[] newElements = new Object[2 * size];

			for (int i = 0; i < capacity; i++) {
				newElements[i] = elements[i];
			}

			elements = newElements;
			capacity *= 2;
		}

		elements[size] = value;
		size++;
	}

	/**
	 * Gets the object stored in the collection at the specified position.
	 * Throws IndexOutOfBoundsException if the index is not reachable.
	 * 
	 * @param index
	 *            Position of the object in array we want to get. Valid indexes
	 *            are 0 to size-1.
	 * @return Returns the object that is stored in backing array at position
	 *         index.
	 * @throws IndexOutOfBoundsException
	 */
	public Object get(int index) throws IndexOutOfBoundsException {

		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("Index out of reach.");
		}

		return elements[index];
	}

	/**
	 * Removes the object that is stored in the backing array at position index.
	 * Throws IndexOutOfBoundsException if the index is not reachable.
	 * 
	 * @param index
	 *            Position of the object in array we want to remove. Valid
	 *            indexes are 0 to size-1.
	 * @throws IndexOutOfBoundsException
	 */
	public void remove(int index) throws IndexOutOfBoundsException {

		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("Index out of reach.");
		}

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}

		size--;
	}

	/**
	 * Inserts the given value at the given position in array. The legal
	 * positions are 0 to size. Greater positions will be shifted. Throws
	 * IllegalArgumentException if the value is null or the position is out of
	 * reach.
	 * 
	 * @throws IllegalArgumentException
	 * @param value
	 *            Object we want to add to collection.
	 * @param position
	 *            Position in array to insert value into.
	 */
	void insert(Object value, int position) throws IllegalArgumentException {

		if (position < 0 || position > size) {
			throw new IllegalArgumentException("Position out of reach.");
		}

		if (value.equals(null)) {
			throw new IllegalArgumentException("\'null\' cannot be stored.");
		}

		// creating new array if the current one is too small
		if (size >= capacity) {

			Object[] newElements = new Object[2 * size];

			for (int i = 0; i < capacity; i++) {
				newElements[i] = elements[i];
			}

			elements = newElements;
			capacity *= 2;
		}

		// shifting greater positions
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}

		elements[position] = value;
		size++;

	}

	/**
	 * Searches the collection and returns the index of the first occurrence of
	 * the given value or -1 if the value is not found.
	 * 
	 * @param value
	 *            Value to search for.
	 * @return Returns the index of the first occurrence of the given value or
	 *         -1 if the value is not found.
	 */
	public int indexOf(Object value) {

		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Searches the collection and returns true if it contains the given value.
	 * 
	 * @param value
	 *            Value to search for.
	 * @return Returns true only if the collection contains given value.
	 */
	public boolean contains(Object value) {

		if (indexOf(value) != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes all elements from the collection. The allocated array is left at
	 * current capacity.
	 */
	void clear() {

		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}

		size = 0;
	}

}
