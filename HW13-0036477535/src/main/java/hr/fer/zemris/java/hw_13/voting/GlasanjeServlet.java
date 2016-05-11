package hr.fer.zemris.java.hw_13.voting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

		String fileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");

		List<String> lines = Files.readAllLines(Paths.get(fileName),
				StandardCharsets.UTF_8);
		Map<String, Band> map = new TreeMap<>();

		for (String line : lines) {
			if (!line.trim().isEmpty()) {
				Band band = new Band(line);
				map.put(band.getiD(), band);
			}
		}

		req.getServletContext().setAttribute("bendovi", map);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(
				req, resp);
	}
}