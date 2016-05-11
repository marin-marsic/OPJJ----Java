<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="java.util.List"%>
<html>

<%
	Long loggedID = (Long) request.getSession().getAttribute(
			"current.user.id");
%>

<head>
<title>Login</title>

<style type="text/css">
.greska {
	font-family: fantasy;
	font-size: 0.9em;
	color: #FF0000;
}
</style>
</head>

<body>
	<h1>Početna stranica</h1>

	<%
		if (loggedID == null) {
	%>

	<form action="loginRedirect" method="post">
		Korisničko ime:<br> <input type="text" name="nick"
			value='<c:out value="${zapis.nick}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('nick')}">
			<div class="greska">
				<c:out value="${zapis.dohvatiPogresku('nick')}" />
			</div>
		</c:if>

		Lozinka:<br> <input type="password" name="password"
			value='<c:out value="${zapis.password}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('password')}">
			<div class="greska">
				<c:out value="${zapis.dohvatiPogresku('password')}" />
			</div>
		</c:if>

		<input type="submit" name="metoda" value="Prijava"> <input
			type="submit" name="metoda" value="Odustani">
	</form>
	<a href="http://localhost:8080/aplikacija5/servleti/registracija">Registracija</a>
	<%
		} else {
			String ime = (String) request.getSession().getAttribute(
					"current.user.fn");
			String prezime = (String) request.getSession().getAttribute(
					"current.user.ln");
	%>

	<p>
		Dobrodošli
		<%=ime%>
		<%=prezime%>!
	</p>
	<a href="http://localhost:8080/aplikacija5/servleti/logout">Odjava</a>
	<%
		}
	%>

	<h4>Bloggeri:</h4>
	<%
		List<BlogUser> users = (List<BlogUser>) request.getServletContext()
				.getAttribute("blogeri");
		for (BlogUser bu : users) {
	%>
	<a
		href="http://localhost:8080/aplikacija5/servleti/autor/<%=bu.getNick()%>"><%=bu.getFirstName()%>
		<%=bu.getLastName()%> (<%=bu.getNick()%>)</a>
	<br>
	<%
		}
	%>


</body>
</html>