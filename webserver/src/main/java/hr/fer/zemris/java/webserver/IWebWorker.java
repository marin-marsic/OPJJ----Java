package hr.fer.zemris.java.webserver;

/**
 * An interface toward any object that can process current request: it gets
 * RequestContext as parameter and it's expected to create a content for
 * client.
 * 
 * @author Marin Maršić
 *
 */
public interface IWebWorker {

	/**
	 * Method which processes the client's request.
	 * @param context
	 */
	public void processRequest(RequestContext context);

}
