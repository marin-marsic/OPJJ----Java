package hr.fer.zemris.java.hw_13.voting;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet which stores current voting results to xls file for user to download.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/vnd.ms-excel");

		@SuppressWarnings("unchecked")
		Map<String, Integer> results = (Map<String, Integer>) req
				.getServletContext().getAttribute("rezultati");
		@SuppressWarnings("unchecked")
		Map<String, Band> bands = (Map<String, Band>) req.getServletContext()
				.getAttribute("bendovi");

		try {
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("0");

			HSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("Bend");
			row.createCell(1).setCellValue("Broj glasova");

			for (String id : results.keySet()) {
				row = sheet.createRow(Integer.parseInt(id) + 1);
				row.createCell(0).setCellValue(bands.get(id).getName());
				row.createCell(1).setCellValue(results.get(id));
			}

			OutputStream out = resp.getOutputStream();
			hwb.write(out);
			out.flush();

			hwb.close();
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(
					req, resp);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}
}