package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;

import javax.swing.JButton;

/**
 * Class representing action which needs to be done after the invert checkbox is
 * selected. The first argument of the method execute is object representing
 * calculator's registers. The second argument of a method execute is a list
 * containing invertible buttons.
 * 
 * @author Marin Maršić
 *
 */
public class InvCheckboxPressed implements ButtonPressed {

	@Override
	public void execute(Registers registers, Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<JButton> buttons = (ArrayList<JButton>) obj;
		registers.setInv(!registers.isInv());

		if (!registers.isInv()) {
			for (JButton b : buttons) {
				if (b.getText().equals("sin")) {
					b.setText("asin");
				} else if (b.getText().equals("cos")) {
					b.setText("acos");
				} else if (b.getText().equals("tan")) {
					b.setText("atan");
				} else if (b.getText().equals("ctg")) {
					b.setText("actg");
				} else if (b.getText().equals("log")) {
					b.setText("10^");
				} else if (b.getText().equals("ln")) {
					b.setText("e^");
				} else if (b.getText().equals("x^n")) {
					b.setText("n\u221Ax");
				}
			}
		} else {
			for (JButton b : buttons) {
				if (b.getText().equals("asin")) {
					b.setText("sin");
				} else if (b.getText().equals("acos")) {
					b.setText("cos");
				} else if (b.getText().equals("atan")) {
					b.setText("tan");
				} else if (b.getText().equals("actg")) {
					b.setText("ctg");
				} else if (b.getText().equals("10^")) {
					b.setText("log");
				} else if (b.getText().equals("e^")) {
					b.setText("ln");
				} else if (b.getText().equals("n\u221Ax")) {
					b.setText("x^n");
				}
			}
		}
	}
}
