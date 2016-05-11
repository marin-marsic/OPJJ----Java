package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.dao.DAOException;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * {@link ServletContextListener} which initializes the database when the server
 * starts.
 * 
 * @author Marin Maršić
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		Properties serverPproperties = new Properties();
		try {
			InputStream input = new FileInputStream(sce.getServletContext()
					.getRealPath("/WEB-INF/dbsettings.properties"));
			serverPproperties.load(input);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading dbsettings.properties.");
		}

		// initialization
		String host = serverPproperties.getProperty("host");
		String port = serverPproperties.getProperty("port");
		String dbName = serverPproperties.getProperty("name");
		String user = serverPproperties.getProperty("user");
		String password = serverPproperties.getProperty("password");

		String connectionURL = "jdbc:derby://" + host + ":" + port + "/"
				+ dbName + ";user=" + user + ";password=" + password + "";

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error initializing the pool.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		try {
			Connection connection = cpds.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, "POLLS", null);
			if (!rs.next()) {
				makePollsTable(connection, sce);
			}
			rs = dbmd.getTables(null, null, "POLLOPTIONS", null);
			if (!rs.next()) {
				makePollOptionsTable(connection, sce);
			}
		} catch (SQLException e) {
		}

	}

	/**
	 * Creates the PollOptions table in the database.
	 * @param con Given connection.
	 * @param sce {@link ServletContextEvent}.
	 */
	private void makePollOptionsTable(Connection con, ServletContextEvent sce) {
		try {
			Statement sta = con.createStatement();
			sta.executeUpdate("CREATE TABLE PollOptions ("
					+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY"
					+ ", optionTitle VARCHAR(100) NOT NULL"
					+ ", optionLink VARCHAR(150) NOT NULL" + ", pollID BIGINT"
					+ ", votesCount BIGINT"
					+ ", FOREIGN KEY (pollID) REFERENCES Polls(id))");
			try {
				sta.close();
			} catch (Exception e) {
			}

		} catch (Exception ex) {
			throw new DAOException("Error creating the 'PollOptions' table", ex);
		}

		String fileName = sce.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");

		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName),
					StandardCharsets.UTF_8);
			for (String line : lines) {
				String[] separated = line.split("\t");
				if (!line.trim().isEmpty()) {
					PreparedStatement pst = null;

					try {
						pst = con.prepareStatement(
								"INSERT INTO PollOptions (optionTitle, optionLink, votesCount, pollID)"
										+ " values (?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
						pst.setString(1, separated[0]);
						pst.setString(2, separated[1]);
						pst.setLong(3, 0);
						pst.setLong(4, Integer.valueOf(separated[2]));

						pst.executeUpdate();

					} catch (SQLException ex) {
						ex.printStackTrace();
					} finally {
						try {
							pst.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
		}

	}

	/**
	 * Creates the Polls table in the database.
	 * @param con Given connection.
	 * @param sce {@link ServletContextEvent}.
	 */
	private void makePollsTable(Connection con, ServletContextEvent sce) {
		try {

			Statement sta = con.createStatement();
			sta.executeUpdate("CREATE TABLE Polls ("
					+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY"
					+ ", title VARCHAR(150) NOT NULL"
					+ ", message CLOB(2048) NOT NULL)");

		} catch (Exception ex) {
			throw new DAOException("Error creating the 'Polls' table", ex);
		}

		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement(
					"INSERT INTO Polls (title, message) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, "Glasanje za omiljeni bend");
			pst.setString(2,
					"Od sljedećih bendova, koji Vam je bend najdraži? "
							+ "Kliknite na link kako biste glasali!.");

			pst.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		try {
			pst = con.prepareStatement(
					"INSERT INTO Polls (title, message) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, "Glasanje za omiljenu Disney princezu");
			pst.setString(2, "Koja Disneyeva princeza si ti?");

			pst.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
