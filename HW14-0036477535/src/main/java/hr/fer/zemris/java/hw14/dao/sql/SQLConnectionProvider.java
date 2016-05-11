package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;

/**
 * Class for storing the connections towards the database into {@link ThreadLocal} object.
 * 
 * @author Marin Maršić
 *
 */
public class SQLConnectionProvider {

	/**
	 * Connections towards the database.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Set connection for the current thread (or remove record from the map if the argument is null).
	 * 
	 * @param con Connection towards the database.
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Get the connection which is available for usage by the current thread.
	 * 
	 * @return Connection towards the database.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}
