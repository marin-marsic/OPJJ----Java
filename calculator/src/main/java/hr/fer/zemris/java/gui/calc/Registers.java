package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;

/**
 * Class representing calculator's registers and memory.
 * 
 * @author Marin Maršić
 *
 */
public class Registers {

	private String display = "";
	private String operator = "";
	private boolean inv = true;
	private boolean newInputExpected = true;
	private boolean decimalPoint = false;
	private ArrayList<Double> stack = new ArrayList<>();

	/**
	 * @return Returns true if the current number on display has decimal point.
	 */
	public boolean isDecimalPoint() {
		return decimalPoint;
	}

	/**
	 * Sets the flag decimal point flag to a given boolean value.
	 * 
	 * @param decimalPoint
	 *            Flag that tells us if the current number on display has
	 *            decimal point.
	 */
	public void setDecimalPoint(boolean decimalPoint) {
		this.decimalPoint = decimalPoint;
	}

	/**
	 * @return Returns true if the new input from user is expected.
	 */
	public boolean isNewInputExpected() {
		return newInputExpected;
	}

	/**
	 * Sets the flag which tells us if the new input is expected.
	 * 
	 * @param newInputExpected
	 *            Flag.
	 */
	public void setNewInputExecuted(boolean newInputExpected) {
		this.newInputExpected = newInputExpected;
	}

	/**
	 * @return Returns true if the invert checkbox is selected.
	 */
	public boolean isInv() {
		return inv;
	}

	/**
	 * Sets the flag which tells us if the invert checkbox is selected.
	 * 
	 * @param inv
	 */
	public void setInv(boolean inv) {
		this.inv = inv;
	}

	/**
	 * @return Returns value shown on display.
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * Sets the display to show a specific value.
	 * 
	 * @param display
	 *            value to show on a display.
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return Returns the stored operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Stores the operator for an future operation.
	 * 
	 * @param operator
	 *            Operator to store.
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Pushes the value on the stack.
	 * 
	 * @param d
	 *            Value to be pushed.
	 */
	public void push(Double d) {
		stack.add(d);
	}

	/**
	 * Pops value from the stack.
	 * 
	 * @return Value popped from the stack.
	 */
	public Double pop() {
		if (stack.isEmpty()) {
			throw new IndexOutOfBoundsException("Stack is empty.");
		}
		Double d = stack.remove(stack.size() - 1);
		return d;
	}

	/**
	 * @return Returns true if the stack is empty.
	 */
	public boolean isEmptyStack() {
		return stack.isEmpty();
	}

	/**
	 * Resets all the calculators registers.
	 */
	public void reset() {
		display = "";
		operator = "";
		inv = false;
		newInputExpected = true;
		decimalPoint = false;
		stack = new ArrayList<>();
	}
}
