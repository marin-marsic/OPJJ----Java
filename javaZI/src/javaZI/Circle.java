package javaZI;

import java.awt.Color;

public class Circle {

	private int x;
	private int y;
	private int radius;
	private Color foreground;
	private Color background;

	public Circle(int x, int y, int radius, String foreground, String background) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.foreground = Color.decode("#" + foreground);
		if (!background.equals("-")) {
			this.background = Color.decode("#" + background);
		} else {
			this.background = Color.decode("#FFFFFF");
		}
	}

	public Circle(int x, int y, int radius, Color foreground, Color background) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.foreground = foreground;
		this.background = background;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public String definicija() {
		return x + " " + y + " " + radius + " "
				+ Integer.toHexString(getForeground().getRGB()).substring(2)
				+ " "
				+ Integer.toHexString(getBackground().getRGB()).substring(2);
	}
}
