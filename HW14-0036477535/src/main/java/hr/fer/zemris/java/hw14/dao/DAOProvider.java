package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.dao.sql.SQLDAO;

/**
 * Singleton class which returns service provider of the data persistence system
 * access.
 * 
 * @author Marin Maršić
 *
 */
public class DAOProvider {

	/**
	 * {@link SQLDAO} instance.
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Method for getting the {@link DAO}.
	 * 
	 * @return Returns instance of class implementing {@link DAO} interface.
	 */
	public static DAO getDao() {
		return dao;
	}

}
