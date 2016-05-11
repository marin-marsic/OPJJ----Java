package hr.fer.zemris.java.tecaj_14.dao;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;

/**
 * Singleton class which returns service provider of the data persistence system
 * access.
 * 
 * @author Marin Maršić
 *
 */
public class DAOProvider {

	/**
	 * {@link JPADAOImpl} instance;
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Method for getting the {@link DAO}.
	 * 
	 * @return Returns instance of class implementing {@link DAO} interface.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}
