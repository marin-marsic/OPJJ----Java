package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the digit button is
 * pressed. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is string
 * representing digit on a button.
 * 
 * @author Marin Maršić
 *
 */
public class DigitPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		if (registers.getDisplay().length() > 1
				&& Character.isLetter(registers.getDisplay().charAt(1))) {
			registers.setDisplay("");
		}
		String digit = (String) obj;
		if (registers.isNewInputExpected()) {
			registers.setDisplay("");
			registers.setNewInputExecuted(false);
		}
		registers.setDisplay(registers.getDisplay().trim() + digit + " ");
	}
}
