package hr.fer.zemris.java.hw_13;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet for creating an xls file for user to download.
 * 
 * @author Marin Maršić
 *
 */
@WebServlet(urlPatterns = { "/powers" })
public class ExcelTable extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int start;
		int end;
		int pages;

		try {
			start = Integer.valueOf(req.getParameter("a"));
		} catch (Exception e) {
			start = -100;
		}

		try {
			end = Integer.valueOf(req.getParameter("b"));
		} catch (Exception e) {
			end = 100;
		}

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		try {
			pages = Integer.valueOf(req.getParameter("n"));
		} catch (Exception e) {
			pages = 1;
		}

		if (start < -100 || start > 100 || end < -100 || end > 100 || pages < 1
				|| pages > 5) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
		}

		resp.setContentType("application/vnd.ms-excel");
		try {
			HSSFWorkbook hwb = new HSSFWorkbook();

			for (int i = 1; i <= pages; i++) {
				HSSFSheet sheet = hwb.createSheet("sheet " + i);

				for (int j = 0; j <= end - start; j++) {
					HSSFRow row = sheet.createRow(j);
					row.createCell(0).setCellValue(j + start);
					row.createCell(1).setCellValue(Math.pow(j + start, i));
				}
			}

			OutputStream out = resp.getOutputStream();
			hwb.write(out);
			out.flush();

			hwb.close();
			req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req,
					resp);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}
}
