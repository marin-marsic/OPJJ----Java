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

		String fileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");

		if (!Files.exists(Paths.get(fileName))) {
			makeFile(req, fileName, resp);
		}

		String requestedID = req.getParameter("id");

		Map<String, Integer> map = makeResultsMap(fileName);
		map.put(requestedID, map.get(requestedID) + 1);

		saveFile(fileName, map);

		req.getServletContext().setAttribute("rezultati", map);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,
				resp);
	}

	/**
	 * Saves the current voting results.
	 * @param fileName Path to the file containing results.
	 * @param map Map containing results.
	 */
	private void saveFile(String fileName, Map<String, Integer> map) {
		StringBuilder strb = new StringBuilder();
		for (String id : map.keySet()) {
			strb.append(id + "\t" + map.get(id) + "\n");
		}

		try {
			Files.write(Paths.get(fileName),
					strb.toString().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
		}
	}

	/**
	 * Reads current results from the file and makes a map.
	 * @param fileName Path to the file containing results. 
	 * @return Returns map containing results.
	 * @throws IOException
	 */
	private Map<String, Integer> makeResultsMap(String fileName)
			throws IOException {
		Map<String, Integer> map = new TreeMap<>();
		List<String> lines = Files.readAllLines(Paths.get(fileName),
				StandardCharsets.UTF_8);
		for (String line : lines) {
			if (!line.trim().isEmpty()) {
				String[] parameters = line.split("\t");
				String iD = parameters[0];
				Integer votes = Integer.parseInt(parameters[1]);
				map.put(iD, votes);
			}
		}
		return map;
	}

	/**
	 * Makes the file containing the results if there isn's any.
	 * @param req {@link HttpServletRequest}.
	 * @param fileName Path to the file containing results.
	 * @param resp {@link HttpServletResponse}.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void makeFile(HttpServletRequest req, String fileName,
			HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		Map<String, Band> map = (Map<String, Band>) req.getServletContext()
				.getAttribute("bendovi");
		if (map == null) {
			req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req,
					resp);
		}

		StringBuilder strb = new StringBuilder();
		for (String id : map.keySet()) {
			strb.append(id + "\t0\n");
		}

		try {
			Files.write(Paths.get(fileName),
					strb.toString().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
		}
	}
}