package hr.fer.zemris.java.tecaj_14.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The only purpose of this servlet is redirect the user of this application to
 * application's default servlet.
 * 
 * @author Marin Maršić
 */
@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {

	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("servleti/main");
	}

}
