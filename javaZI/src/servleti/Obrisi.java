package servleti;

import java.io.IOException;

import javaZI.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/obrisi" })
public class Obrisi extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Model model = (Model) req.getServletContext().getAttribute("model");
		int index = Integer.parseInt(req.getParameter("index"));
		
		model.ukloniKrug(index);
		model.ukloniSelekciju();

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
