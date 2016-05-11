package hr.fer.zemris.java.fractals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexRootedPolynomialTest {

	// test results provided by www.wolframalpha.com

	@Test
	public void applyTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(a, b);
		Complex c = crp.apply(Complex.ONE);
		assertEquals("-9 + 7i", c.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWithNoArguments_ExceptionTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial();
	}

	@Test
	public void toStringTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(a, b);
		Complex c = crp.apply(Complex.ONE);
		assertEquals("(z - (2 - 3i))(z - (-2 - 2i))", crp.toString());
	}
	
	@Test
	public void toComplexPolynomTest() {
		Complex a = new Complex(1, 0);
		Complex b = new Complex(2, 0);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(a, b);
		ComplexPolynomial cp = crp.toComplexPolynom();
		assertEquals("(1 + 0i)*z^2 + (-3 + 0i)*z^1 + (2 + 0i)*z^0", cp.toString());
	}
	
	@Test
	public void indexOfClosestRootFor_indexExists_Test() {
		Complex a = new Complex(1, -5);
		Complex b = new Complex(1, -4.5);
		Complex c = new Complex(1, -4);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(a, b, c);
		int index = crp.indexOfClosestRootFor(new Complex(1, -4.3), 1);
		assertEquals(1, index);
	}
	
	@Test
	public void indexOfClosestRootFor_indexDoesNotExist_Test() {
		Complex a = new Complex(1, -5);
		Complex b = new Complex(1, -4.5);
		Complex c = new Complex(1, -4);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(a, b, c);
		int index = crp.indexOfClosestRootFor(new Complex(1, -4.3), 0.001);
		assertEquals(-1, index);
	}
}
