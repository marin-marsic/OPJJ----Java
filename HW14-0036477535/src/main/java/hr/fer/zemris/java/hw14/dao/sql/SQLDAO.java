package hr.fer.zemris.java.hw14.dao.sql;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.PollEntry;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the DAO system using the SQL technology.
 * 
 * @author Marin Maršić
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollEntry> fetchAllPolls() throws DAOException {
		List<PollEntry> entries = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con
					.prepareStatement("select id, title, message from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollEntry entry = new PollEntry();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setMessage(rs.getString(3));
						entries.add(entry);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while getting polls from database.",
					ex);
		}
		return entries;
	}

	@Override
	public List<PollOptionEntry> fetchAllPollOptions(long pollID)
			throws DAOException {
		List<PollOptionEntry> entries = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con
					.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOptionEntry entry = new PollOptionEntry();
						if (rs.getLong(4) == pollID) {
							entry.setId(rs.getLong(1));
							entry.setOptionTitle(rs.getString(2));
							entry.setOptionLink(rs.getString(3));
							entry.setPollID(rs.getLong(4));
							entry.setVotesCount(rs.getInt(5));
							entries.add(entry);
						}
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while getting polls from database.",
					ex);
		}
		return entries;
	}

	@Override
	public void updateVote(int pollID, int pollOption) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con
					.prepareStatement("UPDATE PollOptions set votesCount=votesCount+1 WHERE id=? AND pollID=?");
			pst.setInt(1, pollOption);
			pst.setInt(2, pollID);
			
			pst.executeUpdate(); 

			try {
				pst.close();
			} catch (Exception ignorable) {
			}

		} catch (Exception ex) {
			throw new DAOException("Error while getting polls from database.",
					ex);
		}
	}

	@Override
	public List<PollOptionEntry> poolResults(long pollID) throws DAOException {
		List<PollOptionEntry> entries = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con
					.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions order by votesCount DESC");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOptionEntry entry = new PollOptionEntry();
						if (rs.getLong(4) == pollID) {
							entry.setId(rs.getLong(1));
							entry.setOptionTitle(rs.getString(2));
							entry.setOptionLink(rs.getString(3));
							entry.setPollID(rs.getLong(4));
							entry.setVotesCount(rs.getInt(5));
							entries.add(entry);
						}
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while getting polls from database.",
					ex);
		}
		return entries;
	}

}
