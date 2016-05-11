package hr.fer.zemris.java.hw_13.voting;

import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Servlet for creating image containing chart representing voting results.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("image/png");
		PieDataset dataset = createDataset(req);
		JFreeChart chart = createChart(dataset, "");
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(400, 400));

		ImageIO.write(chart.createBufferedImage(400, 400), "png",
				resp.getOutputStream());

	}

	/**
	 * Creates data for the chart.
	 * 
	 * @param req
	 *            {@link HttpServletRequest}.
	 * @return Returns created {@link PieDataset}.
	 */
	private PieDataset createDataset(HttpServletRequest req) {
		@SuppressWarnings("unchecked")
		Map<String, Integer> results = (Map<String, Integer>) req
				.getServletContext().getAttribute("rezultati");

		@SuppressWarnings("unchecked")
		Map<String, Band> bands = (Map<String, Band>) req.getServletContext()
				.getAttribute("bendovi");

		DefaultPieDataset result = new DefaultPieDataset();
		for (String id : results.keySet()) {
			result.setValue(bands.get(id).getName(), results.get(id));
		}
		return result;
	}

	/**
	 * Creates chart.
	 * 
	 * @param dataset
	 *            Data for the chart.
	 * @param title
	 *            Title of the chart.
	 * @return {@link JFreeChart} instance.
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true,
				false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setCircular(false);
		plot.setLabelGenerator(null);
		plot.setLabelGap(0.02);
		return chart;

	}
}