<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@page import="java.util.List"%>
<html>

<head>
<title>Entries</title>
</head>

<%
	Long loggedID = (Long) request.getSession().getAttribute(
			"current.user.id");
%>

<body>

	<%
		if (loggedID != null) {
			String ime = (String) request.getSession().getAttribute(
					"current.user.fn");
			String prezime = (String) request.getSession().getAttribute(
					"current.user.ln");
	%>

		Prijavljeni ste kao
		<%=ime%>
		<%=prezime%><br>
	<a href="http://localhost:8080/aplikacija5/servleti/logout">Odjava</a>
	
	<%
		}
	%>
	<a href="http://localhost:8080/aplikacija5">Početna</a>
	<h1>Blogovi korisnika</h1>
	<%
		List<BlogEntry> entries = (List<BlogEntry>) request
				.getServletContext().getAttribute("entries");
		for (BlogEntry be : entries) {
	%>
	<a
		href="http://localhost:8080/aplikacija5/servleti/autor/<%=be.getCreator().getNick()%>/<%=be.getId()%>"><%=be.getTitle()%> </a>
	<br>
	<%
		}
		String nick = (String) request.getAttribute("nick");
		String loggedNick = (String) request.getSession().getAttribute(
				"current.user.nick");
		if (loggedNick != null && loggedNick.equals(nick)) {
	%>
	<br>
	<a
		href="http://localhost:8080/aplikacija5/servleti/autor/<%=nick%>/new">Novi blog</a>
	<br>
	<%
		}
	%>

</body>
</html>