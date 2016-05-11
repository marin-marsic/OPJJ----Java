package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used for generating user's request context.
 * 
 * @author Marin Maršić
 *
 */
public class RequestContext {

	/**
	 * Class representing cookie for our web server.
	 * 
	 * @author Marin Maršić
	 *
	 */
	public static class RCCookie {

		// read-only properties
		/**
		 * Name property.
		 */
		private String name;
		/**
		 * Value of the name property.
		 */
		private String value;
		/**
		 * Domain.
		 */
		private String domain;
		/**
		 * Path.
		 */
		private String path;
		/**
		 * Maximal age property.
		 */
		private Integer maxAge;

		/**
		 * Constructor for {@link RCCookie}.
		 * 
		 * @param name
		 *            Name property.
		 * @param value
		 *            Value of the name property.
		 * @param maxAge
		 *            Maximal age property.
		 * @param domain
		 *            Domain.
		 * @param path
		 *            Path.
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * @return Returns name property.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return Returns value of the name property.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return Returns domain.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return Returns path.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return Returns maximal age property.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}

	/**
	 * Output stream.
	 */
	private OutputStream outputStream;
	/**
	 * Charset of data to send to output stream.
	 */
	private Charset charset;

	// write-only properties
	/**
	 * Encoding of data to send to output stream.
	 */
	private String encoding = "UTF-8";
	/**
	 * Status code.
	 */
	private int statusCode = 200;
	/**
	 * Status text.
	 */
	private String statusText = "OK";
	/**
	 * Mime type.
	 */
	private String mimeType = "text/html";

	// read-only propertie
	/**
	 * Map storing parameters.
	 */
	private Map<String, String> parameters;

	/**
	 * Map storing temporary parameters.
	 */
	private Map<String, String> temporaryParameters;

	/**
	 * Map storing persistant parameters.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * List of output cookies.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Flag which tells us if the header has already been generated.
	 */
	private boolean headerGenerated = false;

	/**
	 * {@link RequestContext} constructor. Output stream cannot be null. If this
	 * is not satisfied, {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param outputStream
	 *            Output stream.
	 * @param parameters
	 *            Parameters.
	 * @param persistentParameters
	 *            PersistentParameters
	 * @param outputCookies
	 *            OutputCookies.
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {

		if (outputStream == null) {
			throw new IllegalArgumentException("OutputStream cannot be null.");
		}
		this.outputStream = outputStream;
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
		temporaryParameters = new HashMap<String, String>();

	}

	/**
	 * @return Returns Output stream.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @return Returns Charset.
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * @return Returns parameters map.
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @return Returns temporary parameters map.
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}

	/**
	 * @return Returns persistent parameters map.
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}

	/**
	 * @return Returns list of output cookies.
	 */
	public List<RCCookie> getOutputCookies() {
		return outputCookies;
	}

	/**
	 * @return Returns true if the header has already been generated.
	 */
	public boolean isHeaderGenerated() {
		return headerGenerated;
	}

	/**
	 * Sets output stream to given one.
	 * 
	 * @param outputStream
	 */
	public void setOutputStream(OutputStream outputStream) {
		if (outputStream == null) {
			throw new IllegalArgumentException("OutputStream cannot be null.");
		}
		this.outputStream = outputStream;
	}

	/**
	 * Sets the charset to given one.
	 * 
	 * @param charset
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Sets encoding. This can only be done if the header hasn't been generated
	 * already. Otherwise {@link RuntimeException} will be thrown.
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header is already generated and cannot be changed.");
		}
		this.encoding = encoding;
	}

	/**
	 * Sets status code . This can only be done if the header hasn't been
	 * generated already. Otherwise {@link RuntimeException} will be thrown.
	 * 
	 * @param encoding
	 */
	public void setStatusCode(int stasusCode) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header is already generated and cannot be changed.");
		}
		this.statusCode = stasusCode;
	}

	/**
	 * Sets status text. This can only be done if the header hasn't been
	 * generated already. Otherwise {@link RuntimeException} will be thrown.
	 * 
	 * @param encoding
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header is already generated and cannot be changed.");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets mime type. This can only be done if the header hasn't been generated
	 * already. Otherwise {@link RuntimeException} will be thrown.
	 * 
	 * @param encoding
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header is already generated and cannot be changed.");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Sets temporary temporary parameters to the given ones.
	 * 
	 * @param temporaryParameters
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Sets temporary temporary parameters to the given ones.
	 * 
	 * @param temporaryParameters
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}

	/**
	 * Sets list of output cookies to the given ones.
	 * 
	 * @param temporaryParameters
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Header is already generated and cannot be changed.");
		}
		this.outputCookies = outputCookies;
	}

	/**
	 * Sets the flag which tells us if the header has already been generated.
	 * 
	 * @param headerGenerated
	 */
	public void setHeaderGenerated(boolean headerGenerated) {
		this.headerGenerated = headerGenerated;
	}

	/**
	 * Method that retrieves value from parameters map (or null if no
	 * association exists).
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Returns value from parameters map mapped to this name.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Method that retrieves names of all parameters in parameters map.
	 * 
	 * @return Read-only set containing all parameters in parameters map.
	 */
	public Set<String> getParameterNames() {
		Set<String> set = new LinkedHashSet<>();
		for (String s : parameters.values()) {
			set.add(s);
		}
		set = Collections.unmodifiableSet(set);
		return set;
	}

	/**
	 * Method that retrieves value from persistentParameters map (or null if no
	 * association exists).
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Returns value from persistentParameters map mapped to this name.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Method that retrieves names of all parameters in persistent parameters
	 * map.
	 * 
	 * @return Read-only set containing all parameters in persistent parameters
	 *         map.
	 */
	public Set<String> getPersistentParameterNames() {
		Set<String> set = new LinkedHashSet<>();
		for (String s : persistentParameters.values()) {
			set.add(s);
		}
		set = Collections.unmodifiableSet(set);
		return set;
	}

	/**
	 * Method that stores a value to persistentParameters map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @param value
	 *            Value to be mapped to name.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Method that removes a value from persistentParameters map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Method that retrieves value from temporaryParameters map (or null if no
	 * association exists).
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Returns value from parameters map mapped to this name.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Method that retrieves names of all parameters in temporary parameters
	 * map.
	 * 
	 * @return Read-only set containing all parameters in temporary parameters
	 *         map.
	 */
	public Set<String> getTemporaryParameterNames() {
		Set<String> set = new LinkedHashSet<>();
		for (String s : temporaryParameters.values()) {
			set.add(s);
		}
		set = Collections.unmodifiableSet(set);
		return set;
	}

	/**
	 * Method that stores a value to temporaryParameters map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @param value
	 *            Value to be mapped to name.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Method that removes a value from temporaryParameters map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Writes it's data into outputStream that was given to RequestContext in
	 * constructor. The first time this method is called, the header will be
	 * generated.
	 * 
	 * @param text
	 *            Text to write to output stream.
	 * @return Returns {@link RequestContext}.
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			String header = makeHeader();
			outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));
			setHeaderGenerated(true);
		}

		try {
			outputStream.write(data);
		} catch (Exception e) {
		}

		return this;
	}

	/**
	 * Writes it's data into outputStream that was given to RequestContext in
	 * constructor converted to bytes by given encoding. The first time this
	 * method is called, the header will be generated.
	 * 
	 * @param text
	 *            Text to write to output stream.
	 * @return Returns {@link RequestContext}.
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			String header = makeHeader();
			outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));
			setHeaderGenerated(true);
		}

		try {
			outputStream.write(text.getBytes(charset));
		} catch (Exception e) {
		}

		return this;
	}

	/**
	 * Method which makes header by given template.
	 * 
	 * @return Returns header.
	 */
	private String makeHeader() {
		charset = Charset.forName(encoding);
		StringBuilder header = new StringBuilder();
		header.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		if (mimeType.startsWith("text")) {
			header.append("Content-Type: " + mimeType + "; charset=" + encoding
					+ "\r\n");
		} else {
			header.append("Content-Type: " + mimeType + "\r\n");
		}

		if (!outputCookies.isEmpty()) {
			for (RCCookie cookie : outputCookies) {
				header.append("Set-Cookie: " + cookie.getName() + "=\""
						+ cookie.getValue() + "\";");

				if (cookie.getDomain() != null) {
					header.append(" Domain=" + cookie.getDomain() + ";");
				}
				if (cookie.getPath() != null) {
					header.append(" Path=" + cookie.getPath() + ";");
				}
				if (cookie.getMaxAge() != null) {
					header.append(" Max-Age=" + cookie.getMaxAge() + ";");
				}

				header.append(" HttpOnly");
				header.append("\r\n");
			}
		}

		header.append("\r\n");
		return header.toString();
	}

	/**
	 * Adds this cookie to list of cookies.
	 * 
	 * @param rcCookie
	 */
	public void addRCCookie(RCCookie rcCookie) {
		if (rcCookie != null) {
			outputCookies.add(rcCookie);
		}
	}
}
