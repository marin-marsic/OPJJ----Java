package hr.fer.zemris.java.tecaj_14.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Entity Manager Factory Provider.
 * 
 * @author Marin Maršić
 *
 */
public class JPAEMFProvider {

	/**
	 * {@link EntityManagerFactory}.
	 */
	public static EntityManagerFactory emf;

	/**
	 * @return Returns {@link EntityManagerFactory} instance.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets the stored {@link EntityManagerFactory} to the given one.
	 * @param emf Given {@link EntityManagerFactory}.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
