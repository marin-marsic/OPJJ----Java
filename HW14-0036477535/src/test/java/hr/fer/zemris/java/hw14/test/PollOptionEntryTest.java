package hr.fer.zemris.java.hw14.test;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import org.junit.Test;

public class PollOptionEntryTest {
	
	@Test
	public void test() {
		PollOptionEntry entry = new PollOptionEntry();
		entry.setId(2);
		entry.setOptionLink("www.com");
		entry.setOptionTitle("naslov");
		entry.setPollID(13);
		entry.setVotesCount(0);
		
		assertEquals(2, entry.getId());
		assertEquals("www.com", entry.getOptionLink());
		assertEquals("naslov", entry.getOptionTitle());
		assertEquals(13, entry.getPollID());
		assertEquals(0, entry.getVotesCount());
		
	}
}
