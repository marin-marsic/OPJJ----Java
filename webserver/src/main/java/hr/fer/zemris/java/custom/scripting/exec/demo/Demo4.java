package hr.fer.zemris.java.custom.scripting.exec.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Program for demonstrating the {@link SmartScriptEngine} by the fourth example
 * given in the homework.
 * 
 * @author Marin Maršić
 *
 */
public class Demo4 {

	/**
	 * Method that executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not needed here.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String documentBody = new String(Files.readAllBytes(Paths
				.get("webroot/scripts/fibonacci.smscr")), StandardCharsets.UTF_8);

		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();

	}
}
