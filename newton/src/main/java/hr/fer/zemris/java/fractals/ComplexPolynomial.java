package hr.fer.zemris.java.fractals;

/**
 * Class representing complex polynomial. It consists of array of factors
 * associated with their potencies. The biggest potency is at the index 0. <br>
 * <br>
 * 
 * For example: polynomial a*z^3 + b*z^2 + c*z + d, where a, b, c, and d are
 * complex factors, would be stored in array as [a, b, c, d].
 * 
 * @author Marin Maršić
 *
 */
public class ComplexPolynomial {

	/**
	 * Array of Complex factors associated with it's potency.
	 */
	private Complex[] factors;

	/**
	 * Constructor of {@link ComplexPolynomial}.
	 * 
	 * @param factors
	 *            Array of complex factors of specific polynomial. For more
	 *            details look at {@link ComplexPolynomial}.
	 */
	public ComplexPolynomial(Complex... factors) {
		this.factors = factors;
	}

	/**
	 * Method that returns complex factor at the given index.
	 * 
	 * @param i
	 *            Specific index.
	 * @return Returns complex factor at the given index.
	 */
	private Complex atIndex(int i) {
		return factors[i];
	}

	/**
	 * @return Returns order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1
	 *         returns 3.
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this polynomial with the given one.
	 * 
	 * @param p
	 *            Polynomial to multiply this one by.
	 * @return Returns new polynomial as a result of multiplication.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newPolinomial = new Complex[this.order() + p.order() + 1];
		for (int i = 0; i < newPolinomial.length; i++) {
			newPolinomial[i] = Complex.ZERO;
		}

		for (int i = 0; i <= order(); i++) {
			for (int j = 0; j <= p.order(); j++) {
				newPolinomial[newPolinomial.length - 1
						- (this.order() + p.order() - i - j)] = newPolinomial[newPolinomial.length
						- 1 - (this.order() + p.order() - i - j)].add(this
						.atIndex(i).multiply(p.atIndex(j)));
			}
		}
		return new ComplexPolynomial(newPolinomial);
	}

	/**
	 * Computes first derivative of this polynomial.<br>
	 * <br>
	 * For example: for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5.
	 * 
	 * @return Returns new polynomial which is derivation of this one.
	 */
	public ComplexPolynomial derive() {
		Complex[] derived = new Complex[factors.length - 1];
		for (int i = 0; i < factors.length - 1; i++) {
			derived[i] = factors[i].multiply(new Complex(order() - i, 0));
		}
		return new ComplexPolynomial(derived);

	}

	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z
	 *            Complex number to calculate polynomial value at.
	 * @return Returns polynomial value at given point as a complex number.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for (int i = 0; i <= order(); i++) {
			Complex c = Complex.ONE;
			for (int j = 0; j < order() - i; j++) {
				c = c.multiply(z);
			}
			c = c.multiply(factors[i]);
			result = result.add(c);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < factors.length; i++) {
			str.append("(");
			str.append(factors[i].toString());
			str.append(")*z^");
			str.append(order() - i);
			if (i < factors.length - 1) {
				str.append(" + ");
			}
		}
		return str.toString();
	}
}
