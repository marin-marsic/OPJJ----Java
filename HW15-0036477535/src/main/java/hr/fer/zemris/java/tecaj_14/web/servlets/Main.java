package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.formular.FormularLogin;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main page of the application.
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/main")
public class Main extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		FormularLogin f = new FormularLogin();
		f.setNick("");
		f.setPassword("");

		req.setAttribute("zapis", f);

		List<BlogUser> users = new JPADAOImpl().getAllUsers();
		req.getServletContext().setAttribute("blogeri", users);

		req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
	}
}
