package hr.fer.zemris.java.hw14.glasanje;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollEntry;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which gives user few choices to vote for.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer pollID = Integer.parseInt(req.getParameter("pollID"));
		List<PollOptionEntry> pollOptions = DAOProvider.getDao().fetchAllPollOptions(pollID);
		List<PollEntry> polls = DAOProvider.getDao().fetchAllPolls();
		

		req.setAttribute("pollOptions", pollOptions);
		req.setAttribute("polls", polls);
		req.getSession().setAttribute("selectedPoll", pollID);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(
				req, resp);
	}
}