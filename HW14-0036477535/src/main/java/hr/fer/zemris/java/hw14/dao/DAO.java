package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.model.PollEntry;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import java.util.List;

/**
 * Interface towards the data persistence system.
 * 
 * @author Marin Maršić
 *
 */
public interface DAO {

	/**
	 * Gets all the created polls from the database.
	 * 
	 * @return Returns list of {@link PollEntry} objects containing informations
	 *         about the polls.
	 * @throws DAOException
	 *             If something goes wrong.
	 */
	public List<PollEntry> fetchAllPolls() throws DAOException;

	/**
	 * Gets all the voting options of a specific poll. The poll is determined by
	 * its pollID.
	 * 
	 * @param pollID
	 *            Index to determine specific poll.
	 * @return Returns list of {@link PollOptionEntry} objects containing
	 *         informations about the specific poll.
	 * @throws DAOException
	 *             If something goes wrong.
	 */
	public List<PollOptionEntry> fetchAllPollOptions(long pollID)
			throws DAOException;

	/**
	 * Gets all the voting results of a specific poll sorted by the number of votes. The poll is determined by
	 * its pollID.
	 * 
	 * @param pollID
	 *            Index to determine specific poll.
	 * @return Returns list of {@link PollOptionEntry} objects containing
	 *         informations about the specific poll.
	 * @throws DAOException
	 *             If something goes wrong.
	 */
	public List<PollOptionEntry> poolResults(long pollID) throws DAOException;

	/**
	 * Increments the number of votes for a specific voting option.
	 * @param pollID Index to determine specific poll.
	 * @param pollOption Index to determine specific voting option.
	 * @throws DAOException If something goes wrong.
	 */
	public void updateVote(int pollID, int pollOption) throws DAOException;

}
