package hr.fer.zemris.java.hw14.test;


import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.hw14.model.PollEntry;

import org.junit.Test;

public class PollEntryTest {
	
	@Test
	public void test() {
		PollEntry entry = new PollEntry();
		entry.setId(2);
		entry.setMessage("poruka");
		entry.setTitle("naslov");
		assertEquals(2, entry.getId());
		assertEquals("poruka", entry.getMessage());
		assertEquals("naslov", entry.getTitle());
	}
}
