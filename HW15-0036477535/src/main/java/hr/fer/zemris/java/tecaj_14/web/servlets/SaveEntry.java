package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.tecaj_14.formular.FormularEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for saving the new entry.
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/saveBlog")
public class SaveEntry extends HttpServlet {

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
			String nick = (String) req.getSession().getAttribute(
					"current.user.nick");
			resp.sendRedirect(req.getServletContext().getContextPath()
					+ "/servleti/autor/" + nick);
			return;
		}

		FormularEntry f = new FormularEntry();
		f.popuniIzHttpRequesta(req);
		f.validiraj();

		if (f.imaPogresaka()) {
			req.setAttribute("zapis", f);
			req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(
					req, resp);
			return;
		}

		String operation = (String) req.getServletContext().getAttribute(
				"operation");

		if (operation.equals("new")) {
			newEntry(f, req, resp);
			return;
		} else if (operation.equals("edit")) {
			edit(f, req, resp);
		}

	}

	/**
	 * Edits wanted entry.
	 * @param f Filled form.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void edit(FormularEntry f, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String id = (String) req.getServletContext().getAttribute("entryID");
		Long entryID = Long.valueOf(id);
		new JPADAOImpl().editEntry(f, entryID);

		String nick = (String) req.getSession().getAttribute("current.user.nick");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/autor/" + nick + "/" + entryID);
	}

	/**
	 * Creates new entry
	 * @param f Filled form.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void newEntry(FormularEntry f, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		Long creatorID = (Long) req.getSession()
				.getAttribute("current.user.id");
		BlogUser user = JPAEMProvider.getEntityManager().find(BlogUser.class,
				creatorID);

		new JPADAOImpl().createNewEntry(f, user);

		String nick = (String) req.getSession().getAttribute("current.user.nick");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/autor/" + nick);
		return;
	}
}
