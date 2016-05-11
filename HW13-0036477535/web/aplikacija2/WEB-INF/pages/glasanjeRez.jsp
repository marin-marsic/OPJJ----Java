<%@page import="java.util.Collections"%>
<%@page import="java.util.LinkedHashSet"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="hr.fer.zemris.java.hw_13.voting.Band"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<%
	String pickedBgCol = (String) request.getSession().getAttribute(
			"pickedBgCol");
	if (pickedBgCol == null) {
		pickedBgCol = "white";
	}

	LinkedHashSet<String> sortedResults = new LinkedHashSet<>();

	Map<String, Band> bands = (Map<String, Band>) request
			.getServletContext().getAttribute("bendovi");

	TreeMap<String, Integer> results = (TreeMap<String, Integer>) request
			.getServletContext().getAttribute("rezultati");

	TreeMap<String, Integer> tempResults = new TreeMap<>();
	for (String id : results.keySet()) {
		tempResults.put(id, results.get(id));
	}

	int maxVotes = 0;
	for (int i = 0; i < results.size(); i++) {
		int max = -1;
		String maxID = null;
		for (String id : tempResults.keySet()) {
			if (max < results.get(id)) {
				max = results.get(id);
				maxID = id;
			}
		}

		if (i == 0) {
			maxVotes = max;
		}
		sortedResults.add(maxID);
		tempResults.remove(maxID);
	}
%>

<body bgcolor="<%=pickedBgCol%>">

	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (String s : sortedResults) {
					String name = bands.get(s).getName();
					Integer votes = results.get(s);
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
		src="http://localhost:8080/aplikacija2/glasanje-grafika" width="400"
		height="400" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="http://localhost:8080/aplikacija2/glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
		<%
			for (String s : sortedResults) {
				String url = bands.get(s).getUrl();
				String name = bands.get(s).getName();
				if (results.get(s) == maxVotes) {
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