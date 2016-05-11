<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogComment"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@page import="java.util.List"%>
<html>

<%
	Long loggedID = (Long) request.getSession().getAttribute(
			"current.user.id");
%>

<head>
<title>Entries</title>
</head>

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
	<%
	
		BlogEntry entry = (BlogEntry)request.getServletContext().getAttribute("entry");
		List<BlogComment> comments = (List<BlogComment>)request.getServletContext().getAttribute("comments");
	%>
			<h1><%=entry.getTitle()%></h1>
			autor: <%=entry.getCreator().getNick()%><br>
			<p><%=entry.getText()%>
			
	<%
		String nick = (String) request.getAttribute("nick");
		String loggedNick = (String) request.getSession().getAttribute(
				"current.user.nick");
		if (loggedNick != null && loggedNick.equals(nick)) {
	%>
	<br>
	<a href="http://localhost:8080/aplikacija5/servleti/autor/<%=nick%>/edit/<%=entry.getId()%>">Uredi</a>
	<br></p>
	<%
		}
	%>
			<h4>Komentari:</h4>
	<%
		for (BlogComment bc : comments){
			
			%>
			<p><%=bc.getMessage()%><br>
			autor: <%=bc.getUsersEMail()%></p>
			<%	
		}
		
	%>
	
	<form action="http://localhost:8080/aplikacija5/servleti/saveComment" method="post">
		Novi komentar:<br> <input type="text" name="message" value='<c:out value="${zapis.message}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('message')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('message')}" /></div>
		</c:if>

		<input type="submit" name="metoda" value="Pohrani">
		
		</form>
		

</body>
</html>