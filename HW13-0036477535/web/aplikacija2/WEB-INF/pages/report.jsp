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

	<h1>OS usage</h1>

	<p>Here are the results of OS usage in survey that we completed.</p>

	<img src="http://localhost:8080/aplikacija2/reportImage" alt="some_text">
	<p>
		<a href="http://localhost:8080/aplikacija2">Return</a>
	</p>

</body>

</html>