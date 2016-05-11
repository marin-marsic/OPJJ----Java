package hr.fer.zemris.java.tecaj_14.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_14.formular.FormularComment;
import hr.fer.zemris.java.tecaj_14.formular.FormularEntry;
import hr.fer.zemris.java.tecaj_14.formular.FormularReg;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

/**
 * Interface towards the data persistence system.
 * 
 * @author Marin Maršić
 *
 */
public interface DAO {

	/**
	 * Fetches the entry with given <code>id</code>. If no such entry exists, the method returns <code>null</code>.
	 * 
	 * @param id Entry's ID.
	 * @return Fetched entry or null if no entry is found.
	 * @throws DAOException If an error occurs.
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Gets {@link BlogEntry} stored in database by nick of user who created it, and its id.
	 * @param nick Nick of the user who created the entry.
	 * @param id ID of the entry.
	 * @return Returns entry.
	 * @throws DAOException If an error occurs.
	 */
	public BlogEntry getUsersEntry(String nick, Long id) throws DAOException;
	
	/**
	 * Finds {@link BlogUser} user by its nick.
	 * @param nick Nick of the user.
	 * @return Returns user.
	 */
	public BlogUser getUserByNick(String nick);
	
	/**
	 * @return Returns list of all registered users.
	 */
	public List<BlogUser> getAllUsers();
	
	/**
	 * Gets all the comments posted on given entry.
	 * @param entry Entry to find comments about.
	 * @return Returns list of all comments posted on given entry.
	 */
	public List<BlogComment> getBlogComments(BlogEntry entry);
	
	/**
	 * Gets all the entries created by the given user.
	 * @param nick Nick of the user.
	 * @return Returns list of all entries created by the given user.
	 */
	public List<BlogEntry> getUsersEntries(String nick);
	
	/**
	 * Creates new user from filled form.
	 * @param f Filled form.
	 */
	public void createNewUser(FormularReg f);
	
	/**
	 * Creates new comment from filled form.
	 * @param f Filled form.
	 * @param email E-mail of user who created the comment.
	 * @param entry Entry to create comment about.
	 */
	public void createNewComment(FormularComment f, String email, BlogEntry entry);
	
	/**
	 * Creates new entry from filled form.
	 * @param f Filled form.
	 * @param user User who created the entry.
	 */
	public void createNewEntry(FormularEntry f, BlogUser user);
	
	/**
	 * Edits given entry from filled form.
	 * @param f Filled form.
	 * @param id ID of the entry.
	 */
	public void editEntry(FormularEntry f, Long id);
}
