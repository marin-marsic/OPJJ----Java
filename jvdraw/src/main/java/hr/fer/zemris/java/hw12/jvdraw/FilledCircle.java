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
 * Class representing drawable filled circle.
 * 
 * @author Marin Maršić
 *
 */
public class FilledCircle extends GeometricalObject {

	/**
	 * Number of created circles.
	 */
	private static int number = 1;
	/**
	 * X-coordinate of center.
	 */
	private int x;
	/**
	 * Y-coordinate of center.
	 */
	private int y;
	/**
	 * Circle's radius.
	 */
	private int radius;
	/**
	 * Color of the fill.
	 */
	private Color background;
	/**
	 * Color of the border.
	 */
	private Color foreground;

	/**
	 * Constructor for the {@link FilledCircle}.
	 * @param x X-coordinate of center.
	 * @param y Y-coordinate of center.
	 * @param radius Circle's radius.
	 * @param foreground Color of the border.
	 * @param background Color of the fill.
	 */
	public FilledCircle(int x, int y, int radius, Color background,
			Color foreground) {
		super("Filled circle " + number);
		number++;

		this.x = x;
		this.y = y;
		this.radius = radius;
		this.foreground = background;
		this.background = foreground;
	}

	/**
	 * Decrements number of created circles by one.
	 */
	public void decrement() {
		number--;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
		g.setColor(foreground);
		g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}

	@Override
	public void change(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));

		JLabel xLabel = new JLabel("x: ");
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField xInput = new JTextField(Integer.valueOf(x).toString());

		JLabel yLabel = new JLabel("y: ");
		yLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField yInput = new JTextField(Integer.valueOf(y).toString());

		JLabel radiusLabel = new JLabel("radius: ");
		radiusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField radiusInput = new JTextField(Integer.valueOf(radius)
				.toString());

		JLabel foregroundLabel = new JLabel("foreground: ");
		foregroundLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JColorArea foregroundArea = new JColorArea(foreground);

		JLabel backgroundLabel = new JLabel("background: ");
		backgroundLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JColorArea backgroundArea = new JColorArea(background);

		panel.add(xLabel);
		panel.add(xInput);
		panel.add(yLabel);
		panel.add(yInput);
		panel.add(radiusLabel);
		panel.add(radiusInput);
		panel.add(foregroundLabel);
		panel.add(foregroundArea);
		panel.add(backgroundLabel);
		panel.add(backgroundArea);

		JOptionPane.showMessageDialog(frame, panel, "Change line",
				JOptionPane.INFORMATION_MESSAGE);

		try {
			x = Integer.parseInt(xInput.getText());
			y = Integer.parseInt(yInput.getText());
			radius = Integer.parseInt(radiusInput.getText());
			background = backgroundArea.getCurrentColor();
			foreground = foregroundArea.getCurrentColor();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Invalid input.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public String getStringRepresentation() {
		String str = "FCIRCLE" + " " + x + " " + y + " " + radius + " "
				+ foreground.getRed() + " " + foreground.getGreen() + " "
				+ foreground.getBlue() + " " + background.getRed() + " "
				+ background.getGreen() + " " + background.getBlue();
		return str;
	}
	
	@Override
	public int minimumX() {
		return x - radius;
	}

	@Override
	public int minimumY() {
		return y - radius;
	}

	@Override
	public int maximumX() {
		return x + radius;
	}

	@Override
	public int maximumY() {
		return y + radius;
	}

	@Override
	public void paintOnImage(Graphics g, int xOffset, int yOffset) {
		int x = this.x + xOffset;
		int y = this.y + yOffset;
		g.setColor(background);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
		g.setColor(foreground);
		g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}
}
