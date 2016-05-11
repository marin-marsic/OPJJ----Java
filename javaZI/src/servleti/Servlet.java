package servleti;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javaZI.Circle;
import javaZI.Model;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/slika" })
public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Model model = (Model) req.getServletContext().getAttribute("model");
		BufferedImage bim = new BufferedImage(500, 500,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bim.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		
		if (model != null){
			for (Circle c : model.getCircles()){
				int x = c.getX();
				int y = c.getY();
				int r = c.getRadius();
				g.setColor(c.getBackground());
				g.fillOval(x - r, y - r, 2 * r, 2 * r);
				g.setColor(c.getForeground());
				g.drawOval(x - r, y - r, 2 * r, 2 * r);
			}
		}

		g.dispose();
		ImageIO.write(bim, "png", resp.getOutputStream());

	}
}
