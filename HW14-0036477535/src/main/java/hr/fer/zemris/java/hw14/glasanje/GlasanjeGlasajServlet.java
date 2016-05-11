package hr.fer.zemris.java.hw14.glasanje;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which stores the user's vote.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer requestedID = Integer.parseInt(req.getParameter("id"));
		Integer pollID = (Integer)(req.getSession().getAttribute("selectedPoll"));
		
		DAOProvider.getDao().updateVote(pollID, requestedID);

		List<PollOptionEntry> poolResults = DAOProvider.getDao().poolResults(pollID);
		req.setAttribute("poolResults", poolResults);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,
				resp);
	}
}