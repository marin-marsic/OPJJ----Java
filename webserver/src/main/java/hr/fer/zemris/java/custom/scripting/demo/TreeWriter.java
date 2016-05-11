package hr.fer.zemris.java.custom.scripting.demo;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Demo program for testing the {@link WriterVisitor} class. I accepts only one argument from
 * command line which is a path to a document with a text to parse.
 * 
 * @author Marin Maršić
 *
 */
public class TreeWriter {

	/**
	 * This method is called once the program is run.
	 * 
	 * @param args Command line arguments.
	 * @throws IOException
	 * @throws SmartScriptParserException
	 */
	public static void main(String[] args) throws IOException, SmartScriptParserException{

		// I'll assume the argument is provided because this is just a tester
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])),
				StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			e.printStackTrace();
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out
					.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}
}
