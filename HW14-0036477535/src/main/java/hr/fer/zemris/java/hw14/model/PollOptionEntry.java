package hr.fer.zemris.java.hw14.model;

/**
 * Class representing PollOptions entries.
 * 
 * @author Marin Maršić
 *
 */
public class PollOptionEntry {
	/**
	 * PollOptions entry's ID.
	 */
	private long id;
	/**
	 * PollOptions entry's title.
	 * 
	 */
	private String optionTitle;
	/**
	 * PollOptions entry's link to a video.
	 */
	private String optionLink;
	/**
	 * ID of a poll this entry belongs to.
	 */
	private long pollID;
	/**
	 * PollOptions entry's number of votes.
	 */
	private int votesCount;

	/**
	 * Default constructor for {@link PollOptionEntry}.
	 */
	public PollOptionEntry() {
	}

	/**
	 * @return Returns PollOptions entry's ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return Returns PollOptions entry's title.
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * @return Returns PollOptions entry's link to a video.
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * @return Returns ID of a poll this entry belongs to.
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * @return Returns entry's number of votes.
	 */
	public int getVotesCount() {
		return votesCount;
	}

	/**
	 * Sets the PollOptions entry's ID.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets the PollOptions entry's title.
	 * 
	 * @param optionTitle
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * Sets the PollOptions entry's link to a video.
	 * 
	 * @param optionLink
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * Sets the ID of a poll this entry belongs to.
	 * 
	 * @param pollID
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * Sets the PollOptions entry's number of votes.
	 * 
	 * @param votesCount
	 */
	public void setVotesCount(int votesCount) {
		this.votesCount = votesCount;
	}

}