package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_14.formular.FormularComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for saving the new comment.
 * @author Marin Maršić
 *
 */
@WebServlet("/servleti/saveComment")
public class SaveComment extends HttpServlet {

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
		if(!"Pohrani".equals(metoda)) {
			BlogEntry entry = (BlogEntry) req.getServletContext().getAttribute("entry");
			String nick =entry.getCreator().getNick();
			String id = String.valueOf(entry.getId());
			resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/autor/" + nick + "/" + id);
			return;
		}

		FormularComment f = new FormularComment();
		f.popuniIzHttpRequesta(req);
		f.validiraj();
		
		if(f.imaPogresaka()) {
			req.setAttribute("zapis", f);
			req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
			return;
		}
		
		String email = (String) req.getSession().getAttribute("current.user.nick");
		if (email == null){
			email = "Anonymous";
		}
		
		BlogEntry entry = (BlogEntry) req.getServletContext().getAttribute("entry");
		
		new JPADAOImpl().createNewComment(f, email, entry);

		String nick =entry.getCreator().getNick();
		String id = String.valueOf(entry.getId());
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/autor/" + nick + "/" + id);
	}
}
