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
%>

<body bgcolor="<%=pickedBgCol%>">

	<h1>The coolest website ever</h1>

	<p>
		<a href="http://localhost:8080/aplikacija2/colors">Background
			color chooser</a>
	</p>
	<p>
		<a href="http://localhost:8080/aplikacija2/funny">Funny story</a>
	</p>
	<p>
		<a href="http://localhost:8080/aplikacija2/trigonometric">Trigonometric values</a>
	</p>

	<p>
		<a href="http://localhost:8080/aplikacija2/report">OS usage</a>
	</p>
	
	<p>
		<a href="http://localhost:8080/aplikacija2/powers?a=1&b=100&n=3">Excel table of powers</a>
	</p>
	
	<p>
		<a href="http://localhost:8080/aplikacija2/appinfo">Server info</a>
	</p>
	
	<p>
		<a href="http://localhost:8080/aplikacija2/glasanje">Glasanje</a>
	</p>

</body>

</html>