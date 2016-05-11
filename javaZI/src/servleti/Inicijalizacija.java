package servleti;

import javaZI.Model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
		Model model = new Model();
		sce.getServletContext().setAttribute("model", model);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
