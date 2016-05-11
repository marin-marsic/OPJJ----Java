package hr.fer.zemris.java.hw14.glasanje;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOptionEntry;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

		Integer pollID = (Integer)(req.getSession().getAttribute("selectedPoll"));
		List<PollOptionEntry> poolResults = DAOProvider.getDao().poolResults(pollID);

		try {
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("0");

			HSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("Odabir");
			row.createCell(1).setCellValue("Broj glasova");

			int index = 1;
			for (PollOptionEntry poe : poolResults) {
				row = sheet.createRow(index++);
				row.createCell(0).setCellValue(poe.getOptionTitle());
				row.createCell(1).setCellValue(poe.getVotesCount());
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