package hr.fer.zemris.java.hw_13;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for remembering background color selected by user.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/setcolor" })
public class SetColor extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String color;
		try {
			color = req.getParameter("color");
		} catch (Exception e) {
			color = "white";
		}

		req.getSession().putValue("pickedBgCol", color);
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
