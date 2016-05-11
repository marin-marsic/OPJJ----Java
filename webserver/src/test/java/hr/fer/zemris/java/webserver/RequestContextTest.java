package hr.fer.zemris.java.webserver;

import static org.junit.Assert.*;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class RequestContextTest {

	@Test(expected = IllegalArgumentException.class)
	public void RequestContext_OutputStreamIsNull_ExceptionTest() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void RequestContext_SetOutputStreamNull_ExceptionTest() {
		RequestContext rc = new RequestContext(System.out,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());

		rc.setOutputStream(System.out);
		rc.setOutputStream(null);
	}

	@Test
	public void RequestContext_MainTest() {
		OutputStream os = System.out;
		RequestContext rc = new RequestContext(os, null, null, null);
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.setPersistentParameters(null);
		rc.setOutputCookies(null);
		rc.setTemporaryParameters(null);

		assertEquals(System.out, rc.getOutputStream());
		assertEquals(null, rc.getParameters());
		assertEquals(null, rc.getPersistentParameters());
		assertEquals(null, rc.getOutputCookies());
		assertEquals(null, rc.getTemporaryParameters());
		assertEquals(null, rc.getOutputCookies());
		
	}
	
	@Test
	public void RequestContext_HeaderGenerated_Test() {
		OutputStream os = System.out;
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		cookies.add(new RCCookie("name", "Pero", 600, "localhost", "index.html"));
		rc.setOutputCookies(cookies);
		rc.addRCCookie(new RCCookie(null, "Mirko", null, null, null));
		rc.addRCCookie(null);

		assertEquals(false, rc.isHeaderGenerated());
		try {
			rc.write("Pero");
			rc.write("Pero".getBytes());
		} catch (IOException e) {
		}
		assertEquals(true, rc.isHeaderGenerated());
	}

	@Test
	public void RCCookieTest() {
		RequestContext.RCCookie cookie = new RequestContext.RCCookie("name",
				"Pero", 600, "localhost", "/abc/");
		assertEquals("name", cookie.getName());
		assertEquals("Pero", cookie.getValue());
		assertEquals(600, cookie.getMaxAge().intValue());
		assertEquals("localhost", cookie.getDomain());
		assertEquals("/abc/", cookie.getPath());
	}
	
	@Test
	public void Charsets_Test() {
		OutputStream os = System.out;
		RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		rc.setCharset(StandardCharsets.UTF_8);
		assertEquals(StandardCharsets.UTF_8, rc.getCharset());
	}
	
	@Test(expected = RuntimeException.class)
	public void SetEncoding_HeaderGeneratedException_Test() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.setOutputCookies(null);
		
		try {
			rc.write("Pero");
		} catch (IOException e) {
		}
		rc.setEncoding("UTF-8");
	}

	@Test(expected = RuntimeException.class)
	public void SetStatusText_HeaderGeneratedException_Test() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		try {
			rc.write("Pero");
		} catch (IOException e) {
		}
		rc.setStatusText("OK");
	}
	
	@Test(expected = RuntimeException.class)
	public void SetStatusCode_HeaderGeneratedException_Test() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		try {
			rc.write("Pero");
		} catch (IOException e) {
		}
		rc.setStatusCode(200);
	}
	
	@Test(expected = RuntimeException.class)
	public void SetMimeType_HeaderGeneratedException_Test() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		rc.setMimeType("text/html");
		
		try {
			rc.write("Pero");
		} catch (IOException e) {
		}
		rc.setMimeType("text/html");
	}
	
	@Test(expected = RuntimeException.class)
	public void SetOutputCookies_HeaderGeneratedException_Test() {
		RequestContext rc = new RequestContext(null,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		rc.setOutputCookies(new ArrayList<RequestContext.RCCookie>());
		
		try {
			rc.write("Pero");
		} catch (IOException e) {
		}
		rc.setOutputCookies(null);
	}
	
	@Test
	public void GetParameters_Test() {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", "Pero");
		RequestContext rc = new RequestContext(System.out,
				parameters, new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		
		rc.setTemporaryParameters(new HashMap<String, String>());
		rc.setPersistentParameter("name", "Mirko");
		rc.setTemporaryParameter("name", "Slavko");
		
		assertEquals("Mirko", rc.getPersistentParameter("name"));
		assertEquals("Slavko", rc.getTemporaryParameter("name"));
		assertEquals("Pero", rc.getParameter("name"));
		
		Set<String> params = rc.getParameterNames();
		Set<String> permParams = rc.getPersistentParameterNames();
		Set<String> tempParams = rc.getTemporaryParameterNames();
		
		assertEquals(true, permParams.contains("Mirko"));
		assertEquals(true, tempParams.contains("Slavko"));
		assertEquals(true, params.contains("Pero"));
		
		rc.removePersistentParameter("name");
		rc.removeTemporaryParameter("name");
		
		assertEquals(true, rc.getPersistentParameters().isEmpty());
		assertEquals(true, rc.getTemporaryParameters().isEmpty());
		
	}
}
