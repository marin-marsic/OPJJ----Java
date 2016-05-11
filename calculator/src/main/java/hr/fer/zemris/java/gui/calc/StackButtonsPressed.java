package hr.fer.zemris.java.gui.calc;

/**
 * Class representing action which needs to be done after the stack button is
 * selected. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is a string
 * representing operation's name ('push' or 'pop').
 * 
 * @author Marin Maršić
 *
 */
public class StackButtonsPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		String buttonName = (String) obj;
		if (buttonName.equals("pop")) {
			try {
				registers.setDecimalPoint(true);
				registers.setDisplay(registers.pop().toString() + " ");
			} catch (Exception e) {
				registers.setDisplay("EMPTY STACK ERROR ");
				registers.setNewInputExecuted(true);
			}
		} else {
			registers.setDecimalPoint(false);
			if (registers.getDisplay().equals("")) {
				return;
			}
			registers.push(Double.parseDouble(registers.getDisplay()));
		}
	}
}
