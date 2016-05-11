package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the function button is
 * pressed. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is string
 * representing function's name (sin, cos, etc.).
 * 
 * @author Marin Maršić
 *
 */
public class FunctionPressed implements ButtonPressed{

	@Override
	public void execute(Registers registers, Object obj) {
		String operator = (String)obj;
		if (registers.getDisplay().equals("") || Character.isLetter(registers.getDisplay().charAt(1))){
			return;
		}
		double value = Double.parseDouble(registers.getDisplay());

		if (operator.equals("asin")) {
			registers.setDisplay(Double.valueOf(Math.asin(value)).toString()
					+ " ");
		} else if (operator.equals("acos")) {
			registers.setDisplay(Double.valueOf(Math.acos(value)).toString()
					+ " ");
		} else if (operator.equals("atan")) {
			registers.setDisplay(Double.valueOf(Math.atan(value)).toString()
					+ " ");
		} else if (operator.equals("actg")) {
			registers.setDisplay(Double.valueOf(1 / Math.atan(value))
					.toString() + " ");
		} else if (operator.equals("1/x")) {
			registers.setDisplay(Double.valueOf(1 / value).toString() + " ");
		} else if (operator.equals("10^")) {
			registers.setDisplay(Double.valueOf(Math.pow(10, value)).toString()
					+ " ");
		} else if (operator.equals("e^")) {
			registers.setDisplay(Double.valueOf(Math.pow(Math.E, value))
					.toString() + " ");
		} else if (operator.equals("+/-")) {
			registers.setDisplay(Double.valueOf(-value).toString() + " ");
		} else if (operator.equals("sin")) {
			registers.setDisplay(Double.valueOf(Math.sin(value)).toString()
					+ " ");
		} else if (operator.equals("cos")) {
			registers.setDisplay(Double.valueOf(Math.cos(value)).toString()
					+ " ");
		} else if (operator.equals("tan")) {
			registers.setDisplay(Double.valueOf(Math.tan(value)).toString()
					+ " ");
		} else if (operator.equals("ctg")) {
			registers.setDisplay(Double.valueOf(1 / Math.tan(value)).toString()
					+ " ");
		} else if (operator.equals("log")) {
			registers.setDisplay(Double.valueOf(Math.log10(value)).toString()
					+ " ");
		} else if (operator.equals("ln")) {
			registers.setDisplay(Double.valueOf(Math.log(value)).toString()
					+ " ");
		}
		registers.setDecimalPoint(true);
		registers.setNewInputExecuted(true);
	}

}
