package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Generates simple HTML page on a client's request. Page shows table containing
 * parameters given by client.
 * 
 * @author Marin Maršić
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		StringBuilder sb = new StringBuilder(
				"<html>\r\n" +
				"  <head>\r\n" + 
				"    <title>Ispis parametara</title>\r\n"+
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <h1>Ispis poslanih parametara</h1>\r\n" +
				"    <table border='1'>\r\n");
		
			for(String name : context.getParameters().keySet()) {
				sb.append("      <tr><td>")
					.append(name)
					.append("</td><td>")
					.append(context.getParameters().get(name))
					.append("</td></tr>\r\n");
			}
			sb.append(
				"    </table>\r\n" + 
				"  </body>\r\n" + 
				"</html>\r\n");
			
			try {
				context.write(sb.toString());
				context.getOutputStream().flush();
				context.getOutputStream().close();
			} catch (IOException e) {
			}
	}

}
