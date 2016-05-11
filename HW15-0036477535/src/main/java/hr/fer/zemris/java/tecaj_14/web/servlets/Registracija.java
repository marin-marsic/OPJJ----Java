package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.formular.FormularReg;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for registration of the new user.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/registracija")
public class Registracija extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		BlogUser user = new BlogUser();
		FormularReg f = new FormularReg();
		f.popuniIzRecorda(user);

		req.setAttribute("zapis", f);

		req.getRequestDispatcher("/WEB-INF/pages/Registracija.jsp").forward(
				req, resp);
	}
}
