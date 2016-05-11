package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Draws a picture of a circle with radius of 100 pixels and colored randomly.
 * 
 * @author Marin Maršić
 *
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		try {
			context.setMimeType("image/png");
			BufferedImage bim = new BufferedImage(200, 200,
					BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = bim.createGraphics();

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bim.getWidth(), bim.getHeight());

			//g.setColor(new Color((float) Math.random(), (float) Math.random(),
					//(float) Math.random()));
			g.setColor(Color.YELLOW);
			g.fillOval(0, 0, bim.getWidth(), bim.getHeight());
			
			g.setColor(Color.WHITE);
			g.fillOval(60, 80, 80, 80);
			
			g.setColor(Color.YELLOW);
			g.fillRect(60, 80, 80, 40);
			
			g.setColor(Color.BLACK);
			g.fillOval(40, 65, 20, 20);
			g.fillOval(140, 65, 20, 20);

			g.dispose();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bim, "png", bos);

			byte[] tijelo = bos.toByteArray();

			context.write(tijelo);
			context.getOutputStream().flush();
			context.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
