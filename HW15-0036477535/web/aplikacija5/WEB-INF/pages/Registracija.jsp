<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Registracija</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-size: 0.9em;
		   color: #FF0000;
		}
		</style>
	</head>

	<body>
		<h1>
		Novi blogger
		</h1>

		<form action="save" method="post">
		
		Ime<br> <input type="text" name="ime" value='<c:out value="${zapis.ime}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('ime')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('ime')}"/></div>
		</c:if>

		Prezime<br> <input type="text" name="prezime" value='<c:out value="${zapis.prezime}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('prezime')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('prezime')}"/></div>
		</c:if>
		
		Nick<br> <input type="text" name="nick" value='<c:out value="${zapis.nick}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('nick')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('nick')}"/></div>
		</c:if>
		
		Lozinka<br> <input type="text" name="password" value='<c:out value="${zapis.password}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('password')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('password')}"/></div>
		</c:if>

		EMail<br> <input type="text" name="email" value='<c:out value="${zapis.email}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('email')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('email')}"/></div>
		</c:if>

		<input type="submit" name="metoda" value="Pohrani">
		<input type="submit" name="metoda" value="Odustani">
		
		</form>

	</body>
</html>