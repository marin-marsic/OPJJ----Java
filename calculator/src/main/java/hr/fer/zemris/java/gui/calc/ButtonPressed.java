package hr.fer.zemris.java.gui.calc;

/**
 * Interface containing method which needs to be executed when the specific
 * calculator's button is pressed.
 * 
 * @author Marin Maršić
 *
 */
public interface ButtonPressed {

	/**
	 * Method implementing action which needs to be done after some button is
	 * pressed.
	 * 
	 * @param registers
	 *            Calculator's registers.
	 * @param obj
	 *            Some other argument. Usually the text written on a button.
	 */
	public void execute(Registers registers, Object obj);
}
