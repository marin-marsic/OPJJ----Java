package hr.fer.zemris.java.tecaj_14.dao.jpa;

import hr.fer.zemris.java.tecaj_14.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Class for storing the connections towards the database into {@link ThreadLocal} object.
 * 
 * @author Marin Maršić
 *
 */
public class JPAEMProvider {

	/**
	 * Connections towards the database.
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * @return Returns {@link EntityManager} instance.
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Closes {@link EntityManager}.
	 * @throws DAOException
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
	/**
	 * Class for storing the {@link EntityManager}.
	 * @author Marin Maršić
	 *
	 */
	private static class LocalData {
		/**
		 * {@link EntityManager}.
		 */
		EntityManager em;
	}
	
}
