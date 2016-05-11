package hr.fer.zemris.java.hw_13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for calculating sine and cosine values for a given range of angles.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/trigonometric" })
public class Trigonometric extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer startFrom = null;
		Integer endAt = null;

		try {
			startFrom = Integer.valueOf(req.getParameter("a"));
		} catch (Exception e) {
			startFrom = 0;
		}

		try {
			endAt = Integer.valueOf(req.getParameter("b"));
		} catch (Exception e) {
			endAt = 360;
		}

		if (startFrom > endAt) {
			Integer tmp = startFrom;
			startFrom = endAt;
			endAt = tmp;
		}

		if (endAt > startFrom + 720) {
			endAt = startFrom + 720;
		}

		List<Values> results = new ArrayList<>();
		for (int i = startFrom, n = endAt.intValue(); i <= n; i++) {
			double degree = Math.PI * i / 180;
			results.add(new Values(i, Math.sin(degree), Math.cos(degree)));
		}

		req.setAttribute("results", results);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(
				req, resp);

	}

	/**
	 * Class representing degree and its sine and cosine value.
	 * 
	 * @author Marin Maršić
	 *
	 */
	public static class Values {
		/**
		 * Degree.
		 */
		private double degree;
		/**
		 * Sine of degree.
		 */
		private double sin;
		/**
		 * Cosine of degree.
		 */
		private double cos;

		/**
		 * Constructor for {@link Values}.
		 * 
		 * @param degree
		 *            Degree.
		 * @param sin
		 *            Sine of degree.
		 * @param cos
		 *            Cosine of degree.
		 */
		public Values(double degree, double sin, double cos) {
			this.degree = degree;
			this.sin = sin;
			this.cos = cos;
		}

		/**
		 * @return Returns degree.
		 */
		public double getDegree() {
			return degree;
		}

		/**
		 * @return Returns sine value of degree.
		 */
		public double getSin() {
			return sin;
		}

		/**
		 * @return Returns cosine value of degree.
		 */
		public double getCos() {
			return cos;
		}
	}
}