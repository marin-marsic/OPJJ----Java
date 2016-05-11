package hr.fer.zemris.java.fractals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexPolynomialTest {

	// test results provided by www.wolframalpha.com 
	
	@Test
	public void orderTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		ComplexPolynomial crp = new ComplexPolynomial(a, b, Complex.ONE_NEG);
		assertEquals(2, crp.order());
	}

	@Test
	public void multiplyTest() {
		Complex a = new Complex(2, 0);
		Complex b = new Complex(3, 0);
		ComplexPolynomial crpA = new ComplexPolynomial(a, a, a);
		ComplexPolynomial crpB = new ComplexPolynomial(b, b, b);
		ComplexPolynomial crpC = crpA.multiply(crpB);
		assertEquals(
				"(6 + 0i)*z^4 + (12 + 0i)*z^3 + (18 + 0i)*z^2 + (12 + 0i)*z^1 + (6 + 0i)*z^0",
				crpC.toString());
	}

	@Test
	public void deriveTest() {
		Complex a = new Complex(7, 2);
		Complex b = new Complex(2, 0);
		Complex c = new Complex(5, 0);
		Complex d = new Complex(1, 0);
		ComplexPolynomial crpA = new ComplexPolynomial(a, b, c, d);
		ComplexPolynomial crpB = crpA.derive();
		
		// (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
		assertEquals("(21 + 6i)*z^2 + (4 + 0i)*z^1 + (5 + 0i)*z^0",
				crpB.toString());
	}
	
	@Test
	public void applyTest() {
		Complex a = new Complex(2, 0);
		Complex b = new Complex(1, 0);
		Complex c = new Complex(1, 0);
		Complex d = new Complex(3, 0);
		ComplexPolynomial crp = new ComplexPolynomial(a, b, c, d);
		Complex result = crp.apply(new Complex(2, 0));
		assertEquals("25 + 0i", result.toString());
	}
}
