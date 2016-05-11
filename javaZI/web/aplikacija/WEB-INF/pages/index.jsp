<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Date"%>
<%@ page import="javaZI.Model"%>
<%@ page import="javaZI.Circle"%>

<html>

<body>

	<h1>Krugovi</h1>

	<p>trenutna slika:</p>

	<a href="http://localhost:8080/aplikacija/obradiKlik">
		<img alt="Slika" src="http://localhost:8080/aplikacija/slika"
			width="500" height="500" ISMAP />
	</a>

	<p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Indeks</th>
				<th>Definicija kruga</th>
				<th>Naredbe</th>
			</tr>
		</thead>
		<tbody>
			<%
				Model model = (Model) request.getServletContext().getAttribute(
						"model");
				for (int i = 0; i < model.getCircles().size(); i++) {
					int index = i;
					String definicija = model.dohvati(i).definicija();
			%>
			<tr>
				<td><%=index%></td>
				<td><%=definicija%></td>
				<td><a href="http://localhost:8080/aplikacija/obrisi?index=<%=index%>">Obriši</a>
					<a href="http://localhost:8080/aplikacija/selektiraj?index=<%=index%>">Selektiraj</a>
				</td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	</p>

	<form action="naredbaRedirect" method="post">
		Naredba:<input type="text" name="naredba"
			value='<c:out value="${zapis.naredba}"/>' size="50">
		<c:if test="${zapis.imaPogresku('naredba')}">
			<div class="greska">
				<c:out value="${zapis.dohvatiPogresku('naredba')}" />
			</div>
		</c:if>

		<input type="submit" name="metoda" value="Izvrši naredbu">
	</form>

</body>

</html>