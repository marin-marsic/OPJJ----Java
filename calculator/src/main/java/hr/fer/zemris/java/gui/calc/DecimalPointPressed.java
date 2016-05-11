package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the decimal point button is
 * pressed. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is decimal
 * point string.
 * 
 * @author Marin Maršić
 *
 */
public class DecimalPointPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		String point = (String) obj;
		if (registers.isDecimalPoint()) {
			return;
		}
		registers.setDisplay(registers.getDisplay().trim() + point + " ");
		registers.setDecimalPoint(true);
	}
}
