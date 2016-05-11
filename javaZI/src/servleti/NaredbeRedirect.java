package servleti;

import java.io.IOException;

import javaZI.Circle;
import javaZI.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/naredbaRedirect" })
public class NaredbeRedirect extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Model model = (Model) req.getServletContext().getAttribute("model");

		Formular f = new Formular();
		f.popuniIzHttpRequesta(req);
		f.validiraj();

		if (f.imaPogresaka()) {
			req.setAttribute("zapis", f);
			req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req,
					resp);
			return;
		}
		
		
		String text = f.getNaredba();
		if (!text.isEmpty()) {
			String[] textSeparated = text.split("\\s+");
			String naredba = textSeparated[0];

			if (naredba.toUpperCase().equals("OBRISI")) {
				if (textSeparated.length > 1) {
					model.ukloniKrug(Integer.parseInt(textSeparated[1]));
				} else {
					model.ukloniKrug(model.getSelected());
				}
			}

			if (naredba.toUpperCase().equals("DODAJ")) {
				int x = Integer.parseInt(textSeparated[1]);
				int y = Integer.parseInt(textSeparated[2]);
				int r = Integer.parseInt(textSeparated[3]);
				String foreground = textSeparated[4];
				String background = textSeparated[5];
				model.dodajKrug(new Circle(x, y, r, foreground, background));
			}

			if (naredba.toUpperCase().equals("selektiraj".toUpperCase())
					&& textSeparated.length > 1) {
				model.postaviSelektirani(Integer.parseInt(textSeparated[1]));
			}

			if (naredba.toUpperCase().equals("deselektiraj".toUpperCase())) {
				model.ukloniSelekciju();
			}
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
