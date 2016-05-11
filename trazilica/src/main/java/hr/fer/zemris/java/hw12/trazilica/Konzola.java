package hr.fer.zemris.java.hw12.trazilica;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Program for simulating simple search engine. User can input four different
 * commands:
 * 
 * <p>
 * query command: Searches for documents containing given words<br>
 * consists of keyword "query" followed by words to search for in documents,
 * </p>
 * 
 * <p>
 * type command: Writes wanted document<br>
 * consists of keyword "type" followed by index number of document in query
 * result,
 * </p>
 * 
 * <p>
 * results command: Writes results of a previous query<br>
 * consists of keyword "results" and no other arguments
 * </p>
 * 
 * <p>
 * results command: Writes results of a previous query<br>
 * consists of keyword "exit" and no other arguments
 * </p>
 * 
 * Results are ordered by similarity to a given search query text. Results show
 * their indexes, their similarity to a given search text, and path to result
 * document.
 * 
 * Path to a directory containing documents needs to be given as a command line
 * argument when starting the program.
 * 
 * @author Marin Maršić
 *
 */
public class Konzola {

	/**
	 * {@link HashSet} containing croatian stop words.
	 */
	private static HashSet<String> stopWords = new HashSet<>();

	/**
	 * {@link HashMap} containing all the non stop words from given documents.
	 */
	private static HashMap<String, Double> vocabulary = new HashMap<>();

	/**
	 * {@link HashMap} associating words with the number of documents they
	 * showed up in.
	 */
	private static HashMap<String, Double> wordsCounter;

	/**
	 * {@link HashMap} associating each document with a new {@link HashMap}
	 * counting number of occurrences of each word.
	 */
	private static HashMap<String, HashMap<String, Double>> dictionary = new HashMap<>();

	/**
	 * Method which executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Path to a directory containing
	 *            documents.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.out
					.println("One argument needed: Path to a directory containing articles.");
		}

		initialize(args[0]);

		System.out.println("The dictionary contains " + vocabulary.size()
				+ " words.\n");

		// read user's input
		Scanner scanner = new Scanner(System.in);
		TreeMap<Double, String> queryResults = null;

		while (true) {
			System.out.printf("Enter command > ");
			String line = scanner.nextLine();
			line = line.trim();

			if (line.isEmpty()) {
				continue;
			}

			String[] lineSeparated = line.split("\\s+");

			String function = lineSeparated[0].trim().toUpperCase();

			if (function.equals("QUERY")) {
				System.out.printf("Query is: [");
				for (int i = 1; i < lineSeparated.length; i++){
					if (i < lineSeparated.length - 1){
						System.out.printf("%s, ", lineSeparated[i]);
					}
					else{
						System.out.printf("%s]\n", lineSeparated[i]);
					}
				}
				queryResults = calculateResult(lineSeparated);
				writeResults(queryResults);

			} else if (function.equals("TYPE")) {
				typeCommand(lineSeparated, queryResults);

			} else if (function.equals("RESULTS")) {
				if (queryResults == null) {
					System.out
							.println("No results to show. Please try querying first.\n");
					continue;
				}
				writeResults(queryResults);

			} else if (function.equals("EXIT")) {
				break;

			} else {
				System.out.println("Invalid keyword. Please try again.\n");
				continue;
			}
		}

		scanner.close();
	}

	/**
	 * Method for calculating the result of a "query" operation.
	 * 
	 * @param lineSeparated
	 *            String array containing user's input.
	 * @return Returns TreeMap of result document paths ordered by their
	 *         similarity to a user's input.
	 */
	private static TreeMap<Double, String> calculateResult(
			String[] lineSeparated) {

		HashMap<String, Double> query = new HashMap<>(vocabulary);
		TreeMap<Double, String> queryResults = new TreeMap<>(
				Collections.reverseOrder());

		for (int i = 1; i < lineSeparated.length; i++) {
			String s = lineSeparated[i].toLowerCase();
			if (query.get(s.toLowerCase()) != null) {
				query.put(s, query.get(s) + 1);
			}
		}

		Double absoluteQueryVector = 0.0;
		for (Double d : query.values()) {
			absoluteQueryVector += d * d;
		}
		absoluteQueryVector = Math.sqrt(absoluteQueryVector);

		for (String path : dictionary.keySet()) {
			HashMap<String, Double> map = dictionary.get(path);

			Double absoluteDocumentVector = 0.0;

			for (Double d : map.values()) {
				absoluteDocumentVector += d * d;
			}
			absoluteDocumentVector = Math.sqrt(absoluteDocumentVector);

			Double scalarProdct = 0.0;

			for (String s : map.keySet()) {
				scalarProdct += map.get(s) * query.get(s);
			}

			Double similarity = scalarProdct
					/ (absoluteDocumentVector * absoluteQueryVector);
			queryResults.put(similarity, path);
		}

		return queryResults;
	}

	/**
	 * Method executing action which needs to be executed on "type" command.
	 * 
	 * @param lineSeparated
	 *            String array containing user's input.
	 * @param queryResults
	 *            TreeMap of result document paths ordered by their similarity
	 *            to a user's input.
	 * @throws IOException
	 */
	private static void typeCommand(String[] lineSeparated,
			TreeMap<Double, String> queryResults) throws IOException {
		if (queryResults == null) {
			System.out
					.println("No results to show. Please try querying first.\n");
			return;
		}
		if (lineSeparated.length != 2) {
			System.out.println("Please select document index to show.\n");
			return;
		}

		try {
			int index = Integer.parseInt(lineSeparated[1]);
			if (index < 0 || index >= queryResults.size()) {
				System.out.println("Invalid index.\n");
				return;
			}
			System.out.println(index);
			writeDocument(index, queryResults);
		} catch (NumberFormatException e) {
			System.out.println("Invalid index.\n");
			return;
		}
	}

	/**
	 * Method for writing text of a result document given by the index.
	 * 
	 * @param index
	 *            Index of a document in result list.
	 * @param queryResults
	 *            TreeMap of result document paths ordered by their similarity
	 *            to a user's input.
	 * @throws IOException
	 */
	private static void writeDocument(int index,
			TreeMap<Double, String> queryResults) throws IOException {
		int i = 0;
		for (Double similarity : queryResults.keySet()) {
			if (i == index) {
				Path path = Paths.get(queryResults.get(similarity));
				System.out.println(path + "\n");
				byte[] bytes = Files.readAllBytes(path);
				String text = new String(bytes, StandardCharsets.UTF_8);
				System.out.println(text.trim());
				System.out.println();
				break;
			}
			i++;
		}
	}

	/**
	 * Method for writing the results of a query operation. It shows first 10
	 * results, or first few results with similarity bigger than 0, whichever is
	 * fewer.
	 * 
	 * @param queryResults
	 *            TreeMap of result document paths ordered by their similarity
	 *            to a user's input.
	 */
	private static void writeResults(TreeMap<Double, String> queryResults) {
		int index = 0;
		for (Double similarity : queryResults.keySet()) {
			if (index < 10 && Math.abs(similarity - 0) > 0.00001)
				System.out.printf("[%d] (%.4f) %s\n", index++, similarity,
						queryResults.get(similarity));
		}
		System.out.println();
	}

	/**
	 * Initializes vocabulary, dictionary, and stop words maps.
	 * 
	 * @param arg
	 *            Path to a directory containing documents.
	 * @throws IOException
	 */
	private static void initialize(String arg) throws IOException {

		// read all stop words
		ReadStopWords();

		File[] files = new File(arg).listFiles();

		// create vocabulary map
		createVocabulary(files);

		// create map with document vectors
		createDictionary(files);

		// update vocabulary to number of documents containing each word
		calculateWordsPerDocument();

		// calculate TFIDF for each word-document pair
		calculateTFIDF();

	}

	/**
	 * Calculates TFIDF vales for each word of each document.
	 */
	private static void calculateTFIDF() {
		for (String path : dictionary.keySet()) {
			HashMap<String, Double> map = dictionary.get(path);
			for (String word : map.keySet()) {

				Double tfidf = Math.log(dictionary.size()
						/ wordsCounter.get(word))
						* map.get(word);
				map.put(word, tfidf);
			}
		}
	}

	/**
	 * Reads and stores stop words to a Set
	 * 
	 * @throws IOException
	 */
	private static void ReadStopWords() throws IOException {
		List<String> stopWordsList = Files.readAllLines(
				Paths.get("./data/zaustavne.txt"), StandardCharsets.UTF_8);

		for (String s : stopWordsList) {
			String word = s.toLowerCase();

			// Throws away dots from the end of the words so we can compare
			// dr. mr. prof. and other similar words.
			if (word.endsWith(".")) {
				word = word.substring(0, word.length() - 1);
			}
			stopWords.add(word.toLowerCase());
		}
	}

	/**
	 * Calculates number of documents each word occurred in.
	 */
	private static void calculateWordsPerDocument() {
		wordsCounter = new HashMap<String, Double>(vocabulary);
		for (String path : dictionary.keySet()) {
			HashMap<String, Double> map = dictionary.get(path);
			for (String word : map.keySet()) {
				if (map.get(word) != 0) {
					wordsCounter.put(word, wordsCounter.get(word) + 1);
				}
			}
		}
	}

	/**
	 * Updates dictionary map with the number of occurrences of each word in
	 * each document.
	 * 
	 * @param files
	 *            Documents to read the words from.
	 * @throws IOException
	 */
	private static void createDictionary(File[] files) throws IOException {
		for (File f : files) {
			dictionary.put(f.getPath(), new HashMap<>(vocabulary));

			String text = new String(Files.readAllBytes(f.toPath()),
					StandardCharsets.UTF_8);
			String[] words = text.split("[^a-zA-ZšđčćžŠĐČĆŽ]");

			for (String s : words) {
				if (s.trim().isEmpty()) {
					continue;
				}
				if (!stopWords.contains(s.toLowerCase())) {
					double number = dictionary.get(f.getPath()).get(
							s.toLowerCase());
					dictionary.get(f.getPath())
							.put(s.toLowerCase(), number + 1);
				}

			}
		}
	}

	/**
	 * Creates vocabulary map from all the documents.
	 * 
	 * @param files
	 *            Documents to read the words from.
	 * @throws IOException
	 */
	private static void createVocabulary(File[] files) throws IOException {
		for (File f : files) {
			String text = new String(Files.readAllBytes(f.toPath()),
					StandardCharsets.UTF_8);
			String[] words = text.split("[^a-zA-ZšđčćžŠĐČĆŽ]");

			for (String s : words) {
				if (s.trim().isEmpty()) {
					continue;
				}
				if (!stopWords.contains(s.toLowerCase())) {
					vocabulary.put(s.toLowerCase(), 0.0);
				}
			}
		}
	}

}
