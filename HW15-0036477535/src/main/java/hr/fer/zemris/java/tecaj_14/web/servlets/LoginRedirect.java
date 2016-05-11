package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.formular.FormularLogin;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for redirecting the user after the login process.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/loginRedirect")
public class LoginRedirect extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}

	/**
	 * Method that does the redirecting.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException if redirecting goes wrong.
	 * @throws IOException if redirecting goes wrong.
	 */
	protected void obradi(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		String metoda = req.getParameter("metoda");
		if (!"Prijava".equals(metoda)) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/main");
			return;
		}

		FormularLogin f = new FormularLogin();
		f.popuniIzHttpRequesta(req);
		f.validiraj();

		if (f.imaPogresaka()) {
			req.setAttribute("zapis", f);
			req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req,
					resp);
			return;
		}

		BlogUser user = new JPADAOImpl().getUserByNick(f.getNick());
		req.getSession().setAttribute("current.user.nick", user.getNick());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.id", user.getId());

		resp.sendRedirect(req.getServletContext().getContextPath()
				+ "/index.html");
	}
}
