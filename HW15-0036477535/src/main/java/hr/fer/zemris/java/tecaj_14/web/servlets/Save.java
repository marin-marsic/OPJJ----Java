package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.formular.FormularReg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for saving the new user.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/save")
public class Save extends HttpServlet {

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
		if (!"Pohrani".equals(metoda)) {
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/main");
			return;
		}

		FormularReg f = new FormularReg();
		f.popuniIzHttpRequesta(req);
		f.validiraj();

		if (f.imaPogresaka()) {
			req.setAttribute("zapis", f);
			req.getRequestDispatcher("/WEB-INF/pages/Registracija.jsp")
					.forward(req, resp);
			return;
		}
		new JPADAOImpl().createNewUser(f);

		resp.sendRedirect(req.getServletContext().getContextPath()
				+ "/index.html");
	}
}
