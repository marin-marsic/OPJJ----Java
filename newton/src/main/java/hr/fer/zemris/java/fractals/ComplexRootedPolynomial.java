package hr.fer.zemris.java.fractals;

/**
 * Class representing complex polynomial which knows all of it's roots.
 * 
 * @author Marin Maršić
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Array to store polynomial roots into.
	 */
	private Complex[] roots;

	/**
	 * Constructor which creates polynomial from the field of it's roots. At
	 * least one root should be given as argument. If no arguments are given,
	 * IllegalArgumentException will be thrown.
	 * 
	 * @throws IllegalArgumentException
	 *             if no roots are given.
	 * @param roots
	 *            Field of polynomial roots.
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		if (roots.length == 0) {
			throw new IllegalArgumentException();
		}
		this.roots = roots;
	}

	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z
	 *            Complex number to calculate polynomial value at.
	 * @return Returns polynomial value at given point as a complex number.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;
		for (Complex c : roots) {
			result = result.multiply(z.sub(c));
		}
		return result;
	}

	/**
	 * Converts this representation to ComplexPolynomial type.
	 * 
	 * @return Returns instance of {@link ComplexPolynomial} class.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial polinom = new ComplexPolynomial(Complex.ONE);
		for (int i = 0; i < roots.length; i++) {
			polinom = polinom.multiply(new ComplexPolynomial(Complex.ONE,
					roots[i].negate()));
		}
		return polinom;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Complex c : roots) {
			str.append("(z - (");
			str.append(c.toString());
			str.append("))");
		}
		return str.toString();
	}

	/**
	 * Finds index of closest root for given complex number z that is within
	 * treshold. If there is no such root, returns -1.
	 * 
	 * @param z
	 *            Complex number to find the closest root to.
	 * @param treshold
	 *            Limit in distance between given complex number, and it's
	 *            closest root.
	 * @return Returns index of closest root for given complex number z that is
	 *         within treshold. If there is no such root, returns -1
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int minIndex = -1;

		double yDistance = roots[0].getImaginary() - z.getImaginary();
		double xDistance = roots[0].getReal() - z.getReal();
		double minDistance = (new Complex(xDistance, yDistance)).module();

		for (int i = 0; i < roots.length; i++) {
			yDistance = roots[i].getImaginary() - z.getImaginary();
			xDistance = roots[i].getReal() - z.getReal();
			double distance = (new Complex(xDistance, yDistance)).module();
			if (distance < minDistance && distance < treshold) {
				minIndex = i;
				minDistance = distance;
			}
		}
		return minIndex;
	}
}
