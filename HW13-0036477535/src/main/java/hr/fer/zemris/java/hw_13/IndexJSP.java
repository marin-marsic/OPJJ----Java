package hr.fer.zemris.java.hw_13;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet calling index.jsp.
 * 
 * @author Marin
 *
 */
@WebServlet(urlPatterns = { "/index.html" })
public class IndexJSP extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}