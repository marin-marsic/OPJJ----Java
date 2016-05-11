package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the operator button is
 * selected. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is a string
 * containing operator ('+', '-', etc.).
 * 
 * @author Marin Maršić
 *
 */
public class OperatorPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		String operator = (String) obj;
		if (registers.getDisplay().equals("")
				|| Character.isLetter(registers.getDisplay().charAt(1))) {
			return;
		}

		else if (!registers.getOperator().equals("")) {
			pressEquals(registers, registers.getOperator());
		}

		else if (!registers.isEmptyStack() && !operator.equals("=")) {
			pressEquals(registers, operator);
		}

		else if (operator.equals("=")) {
			return;
		}

		registers.push(Double.parseDouble(registers.getDisplay()));
		registers.setNewInputExecuted(true);
		registers.setOperator(operator);
		registers.setDecimalPoint(true);
	}

	/**
	 * Method which calculates the result of a one operations stored in
	 * calculator's memory.
	 * 
	 * @param registers
	 *            Calculator's registers.
	 * @param operator
	 *            Operator of an operation to calculate.
	 */
	private static void pressEquals(Registers registers, String operator) {
		double displayValue = Double.parseDouble(registers.getDisplay());
		double memoryValue = registers.pop();

		if (operator.equals("+")) {
			registers.setDisplay(Double.valueOf(displayValue + memoryValue)
					.toString() + " ");
		} else if (operator.equals("-")) {
			registers.setDisplay(Double.valueOf(memoryValue - displayValue)
					.toString() + " ");
		} else if (operator.equals("*")) {
			registers.setDisplay(Double.valueOf(displayValue * memoryValue)
					.toString() + " ");
		} else if (operator.equals("/")) {
			registers.setDisplay(Double.valueOf(memoryValue / displayValue)
					.toString() + " ");
		} else if (operator.equals("x^n")) {
			registers.setDisplay(Double.valueOf(
					Math.pow(memoryValue, displayValue)).toString()
					+ " ");
		} else if (operator.equals("n\u221Ax")) {
			registers.setDisplay(Double.valueOf(
					Math.pow(displayValue, 1 / memoryValue)).toString()
					+ " ");
		}

		registers.setOperator("");
	}
}
