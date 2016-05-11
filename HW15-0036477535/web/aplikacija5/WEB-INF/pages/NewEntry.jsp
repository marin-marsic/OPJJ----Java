<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Novi blog</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-size: 0.9em;
		   color: #FF0000;
		}
		</style>
	</head>

	<body>

		<form action="http://localhost:8080/aplikacija5/servleti/saveBlog" method="post">
		
		Naslov<br> <input type="text" name="title" value='<c:out value="${zapis.title}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('title')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('title')}"/></div>
		</c:if>

		Text<br> <input type="text" name="text" value='<c:out value="${zapis.text}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('text')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('text')}" /></div>
		</c:if>

		<input type="submit" name="metoda" value="Pohrani">
		<input type="submit" name="metoda" value="Odustani">
		
		</form>

	</body>
</html>