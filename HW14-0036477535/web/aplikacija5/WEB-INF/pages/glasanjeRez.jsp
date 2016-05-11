<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.hw14.model.PollOptionEntry"%>
<%@page import="hr.fer.zemris.java.hw14.model.PollEntry"%>
<%@page import="java.util.List"%>

<html>

<%
	List<PollOptionEntry> poolResults = (List<PollOptionEntry>) request
			.getAttribute("poolResults");

	int maxVotes = 0;
%>

<body>

	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Odabir</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (PollOptionEntry poe : poolResults) {
					String name = poe.getOptionTitle();
					Integer votes = poe.getVotesCount();
					if (votes > maxVotes){
						maxVotes = votes;
					}
			%>
			<tr>
				<td><%=name%></td>
				<td><%=votes%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart"
		src="http://localhost:8080/aplikacija5/glasanje-grafika" width="400"
		height="400" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="http://localhost:8080/aplikacija5/glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
		<%
		for (PollOptionEntry poe : poolResults) {
				String url = poe.getOptionLink();
				String name = poe.getOptionTitle();
				if (poe.getVotesCount() == maxVotes) {
		%>
		<h3><%=name%>:</h3>
		<p><iframe width="427" height="255"
				src="https://www.youtube.com/embed/<%=url%>" frameborder="0"
				allowfullscreen></iframe></p>
		<%
			}
			}
		%>

</body>

</html>