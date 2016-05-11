package hr.fer.zemris.java.hw_13;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class implementing {@link ServletContextListener}. Saving current time when starting.
 * @author Marin Maršić
 *
 */
public class ContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		context.removeAttribute("startTime");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		Long time = System.currentTimeMillis();
		context.setAttribute("startTime", time);
		
	}

}
