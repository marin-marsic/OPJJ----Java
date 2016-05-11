package hr.fer.zemris.java.fractals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

	// test results provided by www.wolframalpha.com
	
	@Test
	public void getRealTest() {
		assertEquals(0, new Complex().getReal(), 0.00001);
	}
	
	@Test
	public void getImaginaryTest() {
		assertEquals(0, new Complex().getImaginary(), 0.00001);
	}
	
	@Test
	public void moduleTest() {
		assertEquals(5, new Complex(3, -4).module(), 0.00001);
	}
	
	@Test
	public void multiplyTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		Complex c = a.multiply(b);
		assertEquals(-10, c.getReal(), 0.00001);
		assertEquals(2, c.getImaginary(), 0.00001);
	}
	
	@Test
	public void divisionTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		Complex c = a.divide(b);
		assertEquals(0.25, c.getReal(), 0.00001);
		assertEquals(1.25, c.getImaginary(), 0.00001);
	}
	
	@Test
	public void additionTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		Complex c = a.add(b);
		assertEquals(0, c.getReal(), 0.00001);
		assertEquals(-5, c.getImaginary(), 0.00001);
	}

	@Test
	public void subtractionTest() {
		Complex a = new Complex(2, -3);
		Complex b = new Complex(-2, -2);
		Complex c = a.sub(b);
		assertEquals(4, c.getReal(), 0.00001);
		assertEquals(-1, c.getImaginary(), 0.00001);
	}
	
	@Test
	public void negateTest() {
		Complex a = new Complex(2, -3);
		Complex c = a.negate();
		assertEquals(-2, c.getReal(), 0.00001);
		assertEquals(3, c.getImaginary(), 0.00001);
	}
	
	@Test
	public void toStringTest() {
		Complex a = new Complex(2, -3);
		String string = a.toString();
		assertEquals("2 - 3i", string);
	}
}
