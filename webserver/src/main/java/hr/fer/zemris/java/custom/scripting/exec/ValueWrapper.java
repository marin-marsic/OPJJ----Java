package hr.fer.zemris.java.custom.scripting.exec;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Class representation of wrapper around number value. Wrapper can work only
 * with values of null, Integer, Double or String representation of double or
 * integer number. Class has methods for manipulating the value.
 * 
 * @author Marin Maršić
 *
 */
/**
 * @author Marin
 *
 */
public class ValueWrapper {

	/**
	 * Stored value;
	 */
	private Object value;

	/**
	 * Constructor which gets any object of type Object as argument, but only
	 * stores it if it's value of null, Integer, Double or String. For any other
	 * type of the value, {@link IllegalArgumentException} will be thrown.
	 * 
	 * @throws IllegalArgumentException
	 *             if the type is not supported for wrapping.
	 * @param value
	 *            Value to be wrapped.
	 */
	public ValueWrapper(Object value) {
		if (value != null && !(value instanceof Double)
				&& !(value instanceof Integer) && !(value instanceof String)) {
			throw new IllegalArgumentException("Invalid type of value: "
					+ value);
		}
		this.value = value;
	}

	/**
	 * Method replaces the old value with new one, but only if it's value of
	 * null, Integer, Double or String. For any other type of the value,
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @throws IllegalArgumentException
	 *             if the type is not supported for wrapping.
	 * @param value
	 *            Value to replace the old one.
	 */
	public void setValue(Object value) {
		if (value != null && !(value instanceof Double)
				&& !(value instanceof Integer) && !(value instanceof String)) {
			throw new IllegalArgumentException("Invalid type of value: "
					+ value);
		}
		this.value = value;
	}

	/**
	 * @return Returns the stored value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Method increments the stored value by the given one.
	 * 
	 * @param incValue
	 *            Value to be incremented by.
	 */
	public void increment(Object incValue) {
		resultOperation(value, incValue, (x, y) -> x + y);
	}

	/**
	 * Method decrements the stored value by the given one.
	 * 
	 * @param decValue
	 *            Value to be decremented by.
	 */
	public void decrement(Object decValue) {
		resultOperation(value, decValue, (x, y) -> x - y);
	}

	/**
	 * Method multiplies the stored value by the given one.
	 * 
	 * @param mulValue
	 *            Value to be multiplied by.
	 */
	public void multiply(Object mulValue) {
		resultOperation(value, mulValue, (x, y) -> x * y);
	}

	/**
	 * Method divides the stored value by the given one.
	 * 
	 * @throws ArithmeticException
	 *             if dividing by zero.
	 * @param divValue
	 *            Value to be divided by.
	 */
	public void divide(Object divValue) {
		resultOperation(value, divValue, (x, y) -> {
			if (y.compareTo(Double.valueOf(0)) == 0) {
				throw new ArithmeticException("Dividing by zero.");
			}
			return x / y;
		});
	}

	
	/**
	 * Returns sinus of the value.
	 * 
	 * @param decValue 1.
	 */
	public void sin(Object decValue) {
		resultOperation(value, decValue, (x, y) -> Math.sin(Math.PI*x/180));
	}
	
	/**
	 * Method compares the stored value with the given one.
	 * 
	 * @param withValue
	 *            Value to be compared with
	 * @return Returns result of the comparison in the standard format.
	 */
	public int numCompare(Object withValue) {
		Object valueSave = value;
		int diff;
		resultOperation(value, withValue, (x, y) -> x.compareTo(y));
		if (value instanceof Integer) {
			diff = (Integer) value;
		} else {
			diff = (int) (long) (Math.round((Double) value));
		}
		value = valueSave;
		return diff;
	}

	/**
	 * Method which parses the values if needed to make them numbers. Once that
	 * is done, the given operation can be executed.
	 * 
	 * @param value1
	 *            Stored value.
	 * @param value2
	 *            Value to modify the stored value.
	 * @param operation
	 *            Operation to execute.
	 */
	private void resultOperation(Object value1, Object value2,
			Operation operation) {

		value1 = transform(value1);
		value2 = transform(value2);

		if (value1 instanceof Integer && value2 instanceof Integer) {
			value = new Integer(Double.valueOf(
					operation.execute(Double.valueOf((Integer) value1),
							Double.valueOf((Integer) value2))).intValue());
		} else if (value1 instanceof Integer) {
			value = new Double(operation.execute(
					Double.valueOf((Integer) value1), (Double) value2));
		} else if (value2 instanceof Integer) {
			value = new Double(operation.execute((Double) value1,
					Double.valueOf((Integer) value2)));
		} else {
			value = new Double(operation.execute((Double) value1,
					(Double) value2));
		}
	}

	/**
	 * Converts value from whichever supported type to Integer or Double object.
	 * If that cannot be done, {@link IllegalArgumentException} will be thrown.
	 * 
	 * @throws IllegalArgumentException
	 *             if the value cannot be converted.
	 * @param value
	 *            Value to convert.
	 * @return Returns Object instance of the converted type.
	 */
	private Object transform(Object value) {

		if (value == null) {
			value = new Integer(0);
		} else if (value instanceof String) {
			Scanner scanner = new Scanner((String) value);
			if (scanner.hasNextInt()) {
				value = new Integer(scanner.nextInt());
			} else {
				try {
					NumberFormat nf = NumberFormat
							.getNumberInstance(Locale.ENGLISH);
					DecimalFormat format = (DecimalFormat) nf;
					double number = format.parse(scanner.nextLine())
							.doubleValue();
					value = Double.valueOf(number);
				} catch (ParseException e) {
					throw new IllegalArgumentException("Not supported type: "
							+ value.toString());
				}
			}
		} else if (!(value instanceof Integer) && !(value instanceof Double)) {
			throw new IllegalArgumentException("Not supported type: "
					+ value.toString());
		}

		return value;
	}

	/**
	 * Interface used as a strategy to avoid duplicating the code. It has a
	 * single method representing an operation to be executed.
	 * 
	 * @author Marin Maršić
	 *
	 */
	@FunctionalInterface
	private static interface Operation {
		/**
		 * Executes specific operation over the two given values.
		 * 
		 * @param value1
		 * @param value2
		 * @return Returns the result of the operation.
		 */
		double execute(Double value1, Double value2);
	}
}
