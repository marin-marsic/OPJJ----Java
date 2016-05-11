package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the clear button is
 * pressed. The first argument of the method execute is object representing
 * calculator's registers. The second argument of the method execute is not
 * needed.
 * 
 * @author Marin Maršić
 *
 */
public class ClearButtonPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		registers.setDisplay("");
	}
}
