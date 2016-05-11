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

	Long startTime = (Long) request.getServletContext().getAttribute("startTime");
	
	long now = System.currentTimeMillis() - startTime;

	int millis = (int) now % 1000;
	int sec = (int) now / 1000 % 60;
	int min = (int) now / 1000 / 60 % 60;
	int hrs = (int) now / 1000 / 60 / 60 % 24;
	int days = (int) now / 1000 / 60 / 60 / 24;
	
	String time = days + " days " 
		+ hrs + " hours " 
		+ min + " minutes " 
		+ sec + " seconds " 
		+ millis + " milliseconds";
%>

<body bgcolor="<%=pickedBgCol%>">

	<h1>Server info</h1>

	<p>Application was running for: <%=time%></p>
	<p>
		<a href="http://localhost:8080/aplikacija2">Return</a>
	</p>

</body>

</html>