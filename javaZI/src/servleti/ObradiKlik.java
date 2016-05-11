package servleti;

import java.io.IOException;

import javaZI.Circle;
import javaZI.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/obradiKlik" })
public class ObradiKlik extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Model model = (Model) req.getServletContext().getAttribute("model");

		String point = req.getQueryString().trim();
		String[] separated = point.split(",");
		int x1 = Integer.parseInt(separated[0]);
		int y1 = Integer.parseInt(separated[1]);
		
		int index = model.najblizi(x1, y1);
		if (index >= 0){
			model.postaviSelektirani(index);
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
