package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Class representing drawable line.
 * 
 * @author Marin Maršić
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Number of created lines.
	 */
	private static int number = 1;
	/**
	 * Start x-coordinate of the line.
	 */
	private int x1;
	/**
	 * Start y-coordinate of the line.
	 */
	private int y1;
	/**
	 * End x-coordinate of the line.
	 */
	private int x2;
	/**
	 * End y-coordinate of the line.
	 */
	private int y2;
	/**
	 * Color of the line.
	 */
	private Color color;

	/**
	 * Constructor for the {@link Line}.
	 * @param x1 Start x-coordinate of the line.
	 * @param y1 Start y-coordinate of the line.
	 * @param x2 End x-coordinate of the line.
	 * @param y2 End y-coordinate of the line.
	 * @param color Color of the line.
	 */
	public Line(int x1, int y1, int x2, int y2, Color color) {
		super("Line " + number);
		number++;

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}

	/**
	 * Decrements number of created lines by one.
	 */
	public void decrement() {
		number--;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void change(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));

		JLabel x1Label = new JLabel("x1: ");
		x1Label.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField x1Input = new JTextField(Integer.valueOf(x1).toString());

		JLabel x2Label = new JLabel("x2: ");
		x2Label.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField x2Input = new JTextField(Integer.valueOf(x2).toString());

		JLabel y1Label = new JLabel("y1: ");
		y1Label.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField y1Input = new JTextField(Integer.valueOf(y1).toString());

		JLabel y2Label = new JLabel("y2: ");
		y2Label.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField y2Input = new JTextField(Integer.valueOf(y2).toString());

		JLabel colorLabel = new JLabel("color: ");
		colorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JColorArea colorArea = new JColorArea(color);

		panel.add(x1Label);
		panel.add(x1Input);
		panel.add(x2Label);
		panel.add(x2Input);
		panel.add(y1Label);
		panel.add(y1Input);
		panel.add(y2Label);
		panel.add(y2Input);
		panel.add(colorLabel);
		panel.add(colorArea);

		JOptionPane.showMessageDialog(frame, panel, "Change line",
				JOptionPane.INFORMATION_MESSAGE);

		try {
			x1 = Integer.parseInt(x1Input.getText());
			x2 = Integer.parseInt(x2Input.getText());
			y1 = Integer.parseInt(y1Input.getText());
			y2 = Integer.parseInt(y2Input.getText());
			color = colorArea.getCurrentColor();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Invalid input.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public String getStringRepresentation() {
		String str = "LINE" + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " "
				+ color.getRed() + " " + color.getGreen() + " "
				+ color.getBlue();
		return str;
	}
	
	@Override
	public int minimumX() {
		return Math.min(x1, x2);
	}

	@Override
	public int minimumY() {
		return Math.min(y1, y2);
	}

	@Override
	public int maximumX() {
		return Math.max(x1, x2);
	}

	@Override
	public int maximumY() {
		return Math.max(y1, y2);
	}

	@Override
	public void paintOnImage(Graphics g, int xOffset, int yOffset) {
		g.setColor(color);
		g.drawLine(x1 + xOffset, y1 + yOffset, x2 + xOffset, y2 + yOffset);
	}

}
