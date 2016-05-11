package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Application representing simple calculator with GUI.
 * 
 * @author Marin Maršić
 *
 */
public class Calculator extends JFrame {

	/**
	 * Registers of a calculator.
	 */
	private Registers registers = new Registers();
	private static final long serialVersionUID = 1L;
	/**
	 * Calculator's display.
	 */
	private JLabel label = new JLabel("0 ");
	/**
	 * Checkbox for inverting some operations.
	 */
	private JCheckBox checkbox = new JCheckBox("Inv");
	/**
	 * List containing invertible buttons.
	 */
	private ArrayList<JButton> invertableButtons = new ArrayList<>();

	/**
	 * Constructor of a frame.
	 */
	public Calculator() {
		setLocation(20, 50);
		setTitle("Calculator");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new CalcLayout(7));

		// INITIALIZE ALL THE BUTTONS
		initLabel();
		initDigitButtons();
		initOperatorButtons();
		initFunctions();
		initStackButtons();
		initCheckbox();
		initOtherButtons();

		pack();
		setMinimumSize(this.getMinimumSize());
		setPreferredSize(this.getPreferredSize());
		setMaximumSize(this.getMaximumSize());
	}

	/**
	 * Method for adding checkbox to frame and creating it's
	 * {@link ActionListener}.
	 */
	private void initCheckbox() {
		ActionListener checkboxButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new InvCheckboxPressed();
				bp.execute(registers, invertableButtons);
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		checkbox.addActionListener(checkboxButtonListener);
		this.add(checkbox, "5,7");

	}

	/**
	 * Method for adding push and pop buttons to frame and creating their
	 * {@link ActionListener}.
	 */
	private void initStackButtons() {
		ActionListener stackButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new StackButtonsPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton pushButton = new JButton("push");
		pushButton.addActionListener(stackButtonListener);
		this.add(pushButton, "3,7");

		JButton popButton = new JButton("pop");
		popButton.addActionListener(stackButtonListener);
		this.add(popButton, "4,7");

	}

	/**
	 * Method for adding few buttons to frame and creating their
	 * {@link ActionListener}.
	 */
	private void initOtherButtons() {

		// DECIMAL POINT BUTTON
		ActionListener pointButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new DecimalPointPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton buttonPoint = new JButton(".");
		buttonPoint.addActionListener(pointButtonListener);
		this.add(buttonPoint, "5,5");

		// CLEAR BUTTON
		ActionListener clearButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new ClearButtonPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton buttonClear = new JButton("clr");
		buttonClear.addActionListener(clearButtonListener);
		this.add(buttonClear, "1,7");

		// RESET BUTTON
		ActionListener resetButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new ResetButtonPressed();
				bp.execute(registers, checkbox);
				bp = new InvCheckboxPressed();
				bp.execute(registers, invertableButtons);
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton buttonReset = new JButton("res");
		buttonReset.addActionListener(resetButtonListener);
		this.add(buttonReset, "2,7");

	}

	/**
	 * Method for adding functions buttons to frame and creating their
	 * {@link ActionListener}.
	 */
	private void initFunctions() {
		ActionListener functionButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new FunctionPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton buttonSin = new JButton("sin");
		buttonSin.addActionListener(functionButtonListener);
		invertableButtons.add(buttonSin);
		this.add(buttonSin, "2,2");

		JButton buttonCos = new JButton("cos");
		buttonCos.addActionListener(functionButtonListener);
		invertableButtons.add(buttonCos);
		this.add(buttonCos, "3,2");

		JButton buttonTan = new JButton("tan");
		buttonTan.addActionListener(functionButtonListener);
		invertableButtons.add(buttonTan);
		this.add(buttonTan, "4,2");

		JButton buttonCtg = new JButton("ctg");
		buttonCtg.addActionListener(functionButtonListener);
		invertableButtons.add(buttonCtg);
		this.add(buttonCtg, "5,2");

		JButton buttonInv = new JButton("1/x");
		buttonInv.addActionListener(functionButtonListener);
		this.add(buttonInv, "2,1");

		JButton buttonLog = new JButton("log");
		buttonLog.addActionListener(functionButtonListener);
		invertableButtons.add(buttonLog);
		this.add(buttonLog, "3,1");

		JButton buttonLn = new JButton("ln");
		buttonLn.addActionListener(functionButtonListener);
		invertableButtons.add(buttonLn);
		this.add(buttonLn, "4,1");

		JButton buttonNeg = new JButton("+/-");
		buttonNeg.addActionListener(functionButtonListener);
		this.add(buttonNeg, "5,4");

	}

	/**
	 * Method for adding operator buttons to frame and creating their
	 * {@link ActionListener}.
	 */
	private void initOperatorButtons() {
		ActionListener operatorButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new OperatorPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton buttonEquals = new JButton("=");
		buttonEquals.addActionListener(operatorButtonListener);
		this.add(buttonEquals, "1,6");

		JButton buttonDiv = new JButton("/");
		buttonDiv.addActionListener(operatorButtonListener);
		this.add(buttonDiv, "2,6");

		JButton buttonMul = new JButton("*");
		buttonMul.addActionListener(operatorButtonListener);
		this.add(buttonMul, "3,6");

		JButton buttonSub = new JButton("-");
		buttonSub.addActionListener(operatorButtonListener);
		this.add(buttonSub, "4,6");

		JButton buttonAdd = new JButton("+");
		buttonAdd.addActionListener(operatorButtonListener);
		this.add(buttonAdd, "5,6");

		JButton buttonExp = new JButton("x^n");
		buttonExp.addActionListener(operatorButtonListener);
		invertableButtons.add(buttonExp);
		this.add(buttonExp, "5,1");
	}

	/**
	 * Method for adding digit buttons to frame and creating their
	 * {@link ActionListener}.
	 */
	private void initDigitButtons() {
		ActionListener digitButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonPressed bp = new DigitPressed();
				bp.execute(registers, e.getActionCommand());
				label.setText(registers.getDisplay());
				repaint();
			}
		};

		JButton button0 = new JButton("0");
		button0.addActionListener(digitButtonListener);
		this.add(button0, "5,3");

		JButton button1 = new JButton("1");
		button1.addActionListener(digitButtonListener);
		this.add(button1, "4,3");

		JButton button2 = new JButton("2");
		button2.addActionListener(digitButtonListener);
		this.add(button2, "4,4");

		JButton button3 = new JButton("3");
		button3.addActionListener(digitButtonListener);
		this.add(button3, "4,5");

		JButton button4 = new JButton("4");
		button4.addActionListener(digitButtonListener);
		this.add(button4, "3,3");

		JButton button5 = new JButton("5");
		button5.addActionListener(digitButtonListener);
		this.add(button5, "3,4");

		JButton button6 = new JButton("6");
		button6.addActionListener(digitButtonListener);
		this.add(button6, "3,5");

		JButton button7 = new JButton("7");
		button7.addActionListener(digitButtonListener);
		this.add(button7, "2,3");

		JButton button8 = new JButton("8");
		button8.addActionListener(digitButtonListener);
		this.add(button8, "2,4");

		JButton button9 = new JButton("9");
		button9.addActionListener(digitButtonListener);
		this.add(button9, "2,5");

	}

	/**
	 * Method for adding label to frame and creating it's {@link ActionListener}
	 * .
	 */
	private void initLabel() {
		label.setOpaque(true);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		label.setBackground(Color.ORANGE);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(label, "1,1");
	}

	/**
	 * Main method that executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not needed here.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new Calculator();
			frame.setVisible(true);
		});
	}
}
