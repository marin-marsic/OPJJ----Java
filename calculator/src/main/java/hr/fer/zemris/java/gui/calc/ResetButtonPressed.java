package hr.fer.zemris.java.gui.calc;

import javax.swing.JCheckBox;

/**
 * Class representing action which needs to be done after the reset button is
 * selected. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is a invert
 * checkbox.
 * 
 * @author Marin Maršić
 *
 */
public class ResetButtonPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		JCheckBox chb = (JCheckBox) obj;
		registers.reset();
		if (chb.isSelected()) {
			chb.setSelected(false);
		}
	}
}
