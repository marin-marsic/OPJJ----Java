package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class representing simple server containing all that is needed to support
 * that kind of service. Path to a 'server.properties' file needs to be given
 * througe the command line.
 * 
 * @author Marin Maršić
 *
 */
public class SmartHttpServer {

	/**
	 * Server's IP address.
	 */
	private String address;
	/**
	 * Server's port.
	 */
	private int port;
	/**
	 * Number of threads to serve the clients.
	 */
	private int workerThreads;
	/**
	 * Server's timeout time.
	 */
	private int sessionTimeout;
	/**
	 * Map containing all supported mime types.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Map containing all workers from 'workers.propertie' file.
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();

	/**
	 * Thread that accepts requests from clients.
	 */
	private ServerThread serverThread = new ServerThread();
	/**
	 * Thread pool for worker threads.
	 */
	private ExecutorService threadPool;
	/**
	 * Path to a document client wants to get.
	 */
	private Path documentRoot;

	private volatile Map<String, SessionMapEntry> sessions = new HashMap<>();

	private Random sessionRandom = new Random();

	/**
	 * Constructor for {@link SmartHttpServer}.
	 * 
	 * @throws RuntimeException
	 *             if 'server.properties' cannot be read.
	 * @param configFileName
	 *            relative path to a 'server.properties' file.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public SmartHttpServer(String configFileName)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Properties serverPproperties = new Properties();
		try {
			InputStream input = new FileInputStream(configFileName);
			serverPproperties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading server.properties.");
		}

		// initialization
		address = serverPproperties.getProperty("server.address");
		port = Integer.parseInt(serverPproperties.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverPproperties
				.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(serverPproperties
				.getProperty("session.timeout"))*1000;
		documentRoot = Paths.get(serverPproperties
				.getProperty("server.documentRoot"));

		String mimePrpr = serverPproperties.getProperty("server.mimeConfig");
		String workersProp = serverPproperties.getProperty("server.workers");

		Properties mimeProperties = new Properties();
		Properties workersProperties = new Properties();

		try {
			InputStream input = new FileInputStream(new File(mimePrpr));
			mimeProperties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading mime.properties.");
		}

		try {
			InputStream input = new FileInputStream(workersProp);
			workersProperties.load(input);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading workers.properties.");
		}

		// fill the mime type map
		for (Object key : mimeProperties.keySet()) {
			String mimeKey = (String) key;
			String value = mimeProperties.getProperty(mimeKey);
			mimeTypes.put(mimeKey, value);
		}

		// fill the workers map
		for (Object key : workersProperties.keySet()) {
			String path = (String) key;
			String fqcn = workersProperties.getProperty(path);

			Class<?> referenceToClass = this.getClass().getClassLoader()
					.loadClass(fqcn);
			Object newObject = referenceToClass.newInstance();
			IWebWorker iww = (IWebWorker) newObject;

			if (workersMap.containsKey(path)) {
				throw new RuntimeException(
						"Key from 'workers.properties' file is not unique: "
								+ path);
			}
			workersMap.put(path, iww);
		}
	}

	/**
	 * Method for starting the server.
	 */
	protected synchronized void start() {
		System.out.println("Server started.");
		threadPool = Executors.newFixedThreadPool(workerThreads);
		if (!serverThread.isAlive()) {
			serverThread.start();
		}
	}

	/**
	 * Method for stopping the server.
	 */
	protected synchronized void stop() {
		serverThread.setDaemon(true);
		threadPool.shutdown();
	}

	/**
	 * @author Marin Maršić
	 *
	 */
	private static class SessionMapEntry {
		String sid;
		long validUntil;
		Map<String, String> map;

		public SessionMapEntry(String sid, long validUntil,
				Map<String, String> map) {
			super();
			this.sid = sid;
			this.validUntil = validUntil;
			this.map = map;
		}
	}

	/**
	 * Class which represents server thread accepting clients and delegating the
	 * serving of them to {@link ClientWorker} threads so that the server can
	 * serve more clients at once.
	 * 
	 * @author Marin Maršić
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			
			// timerTask for cleaning sessions map
			Timer timer = new Timer();
			ServerSocket serverSocket = null;
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					Date date = new Date();
					Iterator<Entry<String, SessionMapEntry>> iter = sessions.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, SessionMapEntry> entry = iter.next();
					    if(entry.getValue().validUntil < date.getTime()){
					        iter.remove();
					    }
					}
				}
			}, 300_000, 300_000);
			
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress
						.getByName(address), port));
			} catch (IOException e) {
				throw new RuntimeException("Cannot create server socket.");
			}

			while (true) {
				try {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Class which represents job that needs to be done while serving the client
	 * so that more clients can be served at once.
	 * 
	 * @author Marin Maršić
	 *
	 */
	private class ClientWorker implements Runnable {
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;

		/**
		 * Constructor for {@link ClientWorker}.
		 * 
		 * @param csocket
		 *            Accepted client's socket.
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		@Override
		public void run() {

			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
				List<String> request = readRequest();
				if (request.isEmpty()) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				String firstLine = request.get(0);
				String[] firstLineSeparated = firstLine.split(" ");
				if (firstLineSeparated.length != 3) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				method = firstLineSeparated[0];
				String requestedPath = firstLineSeparated[1];
				version = firstLineSeparated[2];

				if (!method.equalsIgnoreCase("GET")
						|| (!version.equalsIgnoreCase("HTTP/1.0") && !version
								.equalsIgnoreCase("HTTP/1.1"))) {

					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				String path = null;
				String paramString = null;

				String[] requestedPathSeparated = requestedPath.split("\\?");

				path = requestedPathSeparated[0];
				if (requestedPathSeparated.length > 1) {
					paramString = requestedPathSeparated[1];
				}
				
				checkSession(request);
				parseParameters(paramString);
				if (workersMap.containsKey(path)) {
					RequestContext rc = new RequestContext(ostream, params,
							permPrams, outputCookies);
					rc.setStatusCode(200);
					workersMap.get(path).processRequest(rc);
					csocket.close();
					return;
				}

				if (path.startsWith("/ext/")) {
					String className = path.substring(5).trim();
					executeEXTCommand(className);
					csocket.close();
					return;
				}
				requestedPath = Paths.get(documentRoot.toString(), path)
						.toString();

				File requestedFile = new File(requestedPath);
				if (!requestedFile.exists()
						|| !Files.isReadable(Paths.get(requestedPath))) {
					sendError(ostream, 404, "File not found.");
					csocket.close();
					return;
				}

				String extension = requestedFile.getName().split("\\.")[1]
						.trim();
				String mimeType = mimeTypes.get(extension);
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				RequestContext rc = new RequestContext(ostream, params,
						permPrams, outputCookies);
				rc.setMimeType(mimeType);
				rc.setStatusCode(200);

				if (extension.equalsIgnoreCase("SMSCR")) {
					executeScript(requestedFile, rc);
				}
				composeResponse(requestedFile, rc);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Method for finding sid candidate in client's header. If header
		 * contains no sid cookie, or contains one that is too old, new one will
		 * be created.
		 * 
		 * @param request
		 *            List of header lines.
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for (String line : request) {
				if (line.startsWith("Cookie")) {
					String sid = getCookieSid(line);
					sidCandidate = sid;
				}
			}

			if (sidCandidate == null || sidCandidate.equals("null")) {
				Date date = new Date();
				generateSid();

				SessionMapEntry sme = new SessionMapEntry(SID, date.getTime()
						+ sessionTimeout,
						Collections.synchronizedMap(new HashMap<>()));

				RequestContext.RCCookie cookie = new RequestContext.RCCookie(
						"sid", SID, null, address, "/");
				outputCookies.add(cookie);
				sessions.put(SID, sme);
				permPrams = sme.map;
				return;
			}

			for (SessionMapEntry entry : sessions.values()) {
				if (entry.sid.equals(sidCandidate)) {
					Date date = new Date();

					if (entry.validUntil < date.getTime()) {
						SessionMapEntry sme = new SessionMapEntry(entry.sid,
								date.getTime() + sessionTimeout,
								Collections.synchronizedMap(new HashMap<>()));

						sessions.put(sidCandidate, sme);
						permPrams = sme.map;
					} else {
						entry.validUntil = date.getTime() + sessionTimeout;
						sessions.put(sidCandidate, entry);
						permPrams = entry.map;
					}
				}
			}
		}

		/**
		 * Method for generating sid made out of 20 uppercase letters.
		 * 
		 * @return Returns generated sid.
		 */
		private void generateSid() {
			StringBuilder sid = new StringBuilder();
			String alphabet = "ABCDEFGHIJKLMNOPRSTZVXYWZ";
			for (int i = 0; i < 20; i++) {
				sid.append(alphabet.charAt(sessionRandom.nextInt(alphabet
						.length())));
			}

			SID = sid.toString();
		}

		/**
		 * Returns list of {@link RCCookie} objects parsed from given line.
		 * 
		 * @param line
		 *            Line containing formated cookies.
		 * @return Returns list of {@link RCCookie} objects parsed from given
		 *         line.
		 */
		private String getCookieSid(String line) {
			line = line.substring(8);
			String[] onlyCookies = line.split(" ");
			line = onlyCookies[0];
			String[] cookies = line.split(";");

			for (String cookie : cookies) {
				String[] cookieSplitted = cookie.split("=");
				String name = cookieSplitted[0].trim();
				String value = cookieSplitted[1].trim();
				value = value.substring(1, value.length()-1);
				if (name.equals("sid")) {
					return value;
				}
			}

			return null;
		}

		/**
		 * Method for serving client's request containing '/ext/XXX'.
		 * 
		 * @param className
		 *            Name of the {@link IWebWorker} instance client wants to
		 *            execute.
		 */
		private void executeEXTCommand(String className) {
			RequestContext rc = new RequestContext(ostream, params, permPrams,
					outputCookies);
			rc.setStatusCode(200);

			try {
				Class<?> ref = Class
						.forName("hr.fer.zemris.java.webserver.workers."
								+ className);
				IWebWorker worker = (IWebWorker) ref.newInstance();
				worker.processRequest(rc);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			return;
		}

		/**
		 * Method which executes .smscr scripts on server and returns the result
		 * to client.
		 * 
		 * @param requestedFile
		 *            Requested file by client.
		 * @param rc
		 *            {@link RequestContext} object of a client being served.
		 * @throws IOException
		 *             if the requested file cannot be read.
		 */
		private void executeScript(File requestedFile, RequestContext rc)
				throws IOException {
			String documentBody;
			documentBody = new String(Files.readAllBytes(Paths
					.get(requestedFile.getPath())), StandardCharsets.UTF_8);

			new SmartScriptEngine(
					new SmartScriptParser(documentBody).getDocumentNode(), rc)
					.execute();

			try {
				ostream.flush();
				ostream.close();
			} catch (IOException e) {
			}
		}

		/**
		 * Method which sends result of the request to the client alongside with
		 * the header.
		 * 
		 * @param file
		 *            File requested by user.
		 * @param rc
		 *            {@link RequestContext} object of a client being served.
		 * @throws IOException
		 *             in case of error while writing data to output stream.
		 */
		private void composeResponse(File file, RequestContext rc)
				throws IOException {
			byte[] bytes = Files.readAllBytes(file.toPath());
			rc.write(bytes);
			ostream.flush();
			ostream.close();
		}

		/**
		 * Method for parsing parameters given by the client.
		 * 
		 * @param paramString
		 *            string containing parameters.
		 */
		private void parseParameters(String paramString) {
			if (paramString == null) {
				return;
			}
			String[] paramStringSeparated = paramString.split("&");
			for (String s : paramStringSeparated) {
				String[] entry = s.split("=");
				params.put(entry[0], entry[1]);
			}

		}

		/**
		 * Method for sending error message to client.
		 * 
		 * @param cos
		 *            Client's output stream.
		 * @param statusCode
		 *            Status code of the error.
		 * @param statusText
		 *            Text of the error.
		 * @throws IOException
		 */
		private void sendError(OutputStream cos, int statusCode,
				String statusText) throws IOException {

			cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
					+ "Server: simple java server\r\n"
					+ "Content-Type: text/html;charset=UTF-8\r\n"
					+ "Connection: close\r\n" + "\r\n")
					.getBytes(StandardCharsets.US_ASCII));

			String body = "<html>" + "<body>" + "<h1>Error: " + statusCode
					+ "</h1>" + "<p>" + statusText + "</p></body></html>";
			cos.write(body.getBytes(StandardCharsets.US_ASCII));
			cos.flush();
			cos.close();
		}

		/**
		 * Method for parsing header obtained from client. Stores lines of the
		 * header to a list of strings.
		 * 
		 * @return List of header lines.
		 * @throws IOException
		 */
		private List<String> readRequest() throws IOException {

			// read the header as a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = istream.read();
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}

			// convert header to string
			String request = new String(bos.toByteArray(),
					StandardCharsets.US_ASCII);

			// split header by lines
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : request.split("\n")) {
				if (s.isEmpty())
					break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}

			return headers;
		}
	}

	/**
	 * Method which executes once the program starts. Starts the server.
	 * 
	 * @param args
	 *            Command line arguments. Path to a 'server.properties' file.
	 */
	public static void main(String[] args) {

		SmartHttpServer httpServer;
		try {
			httpServer = new SmartHttpServer(args[0]);
			httpServer.start();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			System.err.println(e.getMessage());
		}
	}

}
