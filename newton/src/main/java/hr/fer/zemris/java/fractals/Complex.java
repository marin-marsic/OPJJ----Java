package hr.fer.zemris.java.fractals;

import java.text.DecimalFormat;

/**
 * Class which represents an implementation of the unmodifiable complex number.
 * Each method which performs some kind of modification must return a new
 * instance which represents modified number.
 * 
 * @author Marin Maršić
 *
 */
public class Complex {

	/**
	 * Real part of the complex number.
	 */
	private double real;
	/**
	 * Imaginary part of the complex number.
	 */
	private double imaginary;

	/**
	 * 0 + 0i
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * 1 + 0i
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * -1 + 0i
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * 0 + 1i
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * 0 - 1i
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Default constructor for complex number which initializes both real and
	 * imaginary part to zero.
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructor for complex number which initializes both real and imaginary
	 * part by the given values.
	 * 
	 * @param re
	 *            Real part of the complex number.
	 * @param im
	 *            Imaginary part of the complex number.
	 */
	public Complex(double re, double im) {
		real = re;
		imaginary = im;
	}

	/**
	 * @return Returns real part of the complex number.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * @return Returns imaginary part of the complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * @return Returns module of complex number.
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);

	}

	/**
	 * Multiplies complex number by the given one.
	 * 
	 * @param c
	 *            Complex number to multiply by.
	 * @return Returns new complex number as a result of multiplication.
	 */
	public Complex multiply(Complex c) {
		double imaginary = this.real * c.getImaginary() + this.imaginary
				* c.getReal();
		double real = this.real * c.getReal() - this.imaginary
				* c.getImaginary();
		return new Complex(real, imaginary);
	}

	/**
	 * Divides complex number by the given one.
	 * 
	 * @throws ArithmeticException
	 *             in case of dividing by zero.
	 * @param c
	 *            Complex number to divide by.
	 * @return Returns new complex number as a result of division.
	 */
	public Complex divide(Complex c) {
		double imaginary = this.real * -c.getImaginary() + this.imaginary
				* c.getReal();
		double real = this.real * c.getReal() + this.imaginary
				* c.getImaginary();

		real /= (c.module() * c.module());
		imaginary /= (c.module() * c.module());

		return new Complex(real, imaginary);
	}

	/**
	 * Adds complex number to the given one.
	 * 
	 * @param c
	 *            Complex number to add.
	 * @return Returns new complex number as a result of addition.
	 */
	public Complex add(Complex c) {
		double real = this.real + c.getReal();
		double imaginary = this.imaginary + c.getImaginary();
		return new Complex(real, imaginary);
	}

	/**
	 * Subtracts complex number by the given one.
	 * 
	 * @param c
	 *            Complex number to subtract.
	 * @return Returns new complex number as a result of subtraction.
	 */
	public Complex sub(Complex c) {
		double real = this.real - c.getReal();
		double imaginary = this.imaginary - c.getImaginary();
		return new Complex(real, imaginary);
	}

	/**
	 * @return Returns negated complex number.
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
	}

	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("#.##");
		double real = this.getReal();
		double imaginary = this.getImaginary();
		String prefix = " - ";

		if (this.getImaginary() >= 0) {
			prefix = " + ";
		}
		return formatter.format(real) + prefix
				+ formatter.format(Math.abs(imaginary)) + "i";
	}
}
