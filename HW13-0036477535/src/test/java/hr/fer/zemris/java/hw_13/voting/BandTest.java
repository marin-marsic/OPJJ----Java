package hr.fer.zemris.java.hw_13.voting;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for {@link Band} class.
 * 
 * @author Marin Maršić
 *
 */
public class BandTest {

	/**
	 * Test for constructor which accepts 3 arguments.
	 */
	@Test
	public void Constructor_1_test() {
		Band band = new Band("1", "Queen", "www.queen.com");
		assertEquals("1", band.getiD());
		assertEquals("Queen", band.getName());
		assertEquals("www.queen.com", band.getUrl());
	}
	
	/**
	 * Test for constructor which accepts single argument.
	 */
	@Test
	public void Constructor_2_test() {
		Band band = new Band("1\tQueen\twww.queen.com");
		assertEquals("1", band.getiD());
		assertEquals("Queen", band.getName());
		assertEquals("www.queen.com", band.getUrl());
	}
}
