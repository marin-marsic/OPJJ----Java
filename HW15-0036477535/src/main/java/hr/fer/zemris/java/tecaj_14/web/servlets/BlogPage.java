package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.formular.FormularEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which manages the entries requests.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/autor/*")
public class BlogPage extends HttpServlet {

	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String info = req.getPathInfo();

		info = info.substring(1).trim();

		if (!info.contains("/")) {
			listBlogs(req, info, resp);
			return;
		} else {
			String[] infoSplitted = info.split("/");

			if (infoSplitted[1].equals("new")) {
				createNewBlog(infoSplitted, req, resp);
				return;
			} else if (infoSplitted[1].equals("edit")) {
				editBlog(infoSplitted, req, resp);
				return;
			} else {
				openBlog(infoSplitted, req, resp);
				return;
			}
		}

	}

	/**
	 * Shows requested blog.
	 * @param infoSplitted Path informations splitted by '/'.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException if redirecting goes wrong.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void openBlog(String[] infoSplitted, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException{

		String nick = infoSplitted[0].trim();
		Long id = Long.valueOf(infoSplitted[1].trim());
		BlogEntry entry = new JPADAOImpl().getUsersEntry(nick, id);

		List<BlogComment> comments = new JPADAOImpl().getBlogComments(entry);
		req.setAttribute("nick", infoSplitted[0]);
		req.getServletContext().setAttribute("entry", entry);
		req.getServletContext().setAttribute("comments", comments);
		req.getRequestDispatcher("/WEB-INF/pages/Entry.jsp").forward(req, resp);
		return;
	}

	/**
	 * Edits wanted blog.
	 * @param infoSplitted Path informations splitted by '/'.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException if redirecting goes wrong.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void editBlog(String[] infoSplitted, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		String nick = infoSplitted[0].trim();
		Long id = Long.valueOf(infoSplitted[2].trim());
		BlogEntry entry = new JPADAOImpl().getUsersEntry(nick, id);
		FormularEntry f = new FormularEntry();
		f.popuniIzRecorda(entry);

		req.getServletContext().setAttribute("entryID", infoSplitted[2]);
		req.getServletContext().setAttribute("operation", "edit");
		req.setAttribute("zapis", f);

		req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req,
				resp);
		return;
	}

	/**
	 * Creates new blog.
	 * @param infoSplitted Path informations splitted by '/'.
	 * @param req {@link HttpServletRequest}.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException if redirecting goes wrong.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void createNewBlog(String[] infoSplitted, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		BlogEntry entry = new BlogEntry();
		FormularEntry f = new FormularEntry();
		f.popuniIzRecorda(entry);

		req.getServletContext().setAttribute("operation", "new");
		req.setAttribute("zapis", f);

		req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req,
				resp);
		return;
	}

	/**
	 * Lists all the blogs of the user.
	 * @param req {@link HttpServletRequest}.
	 * @param nick Nick of the user.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException if redirecting goes wrong.
	 * @throws IOException if redirecting goes wrong.
	 */
	private void listBlogs(HttpServletRequest req, String nick,
			HttpServletResponse resp) throws ServletException, IOException {

		List<BlogEntry> entries = new JPADAOImpl().getUsersEntries(nick);
		req.setAttribute("nick", nick);
		req.getServletContext().setAttribute("entries", entries);
		req.getRequestDispatcher("/WEB-INF/pages/Entries.jsp").forward(req,
				resp);
		return;
	}

}
