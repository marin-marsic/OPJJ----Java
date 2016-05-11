<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.hw14.model.PollOptionEntry"%>
<%@page import="hr.fer.zemris.java.hw14.model.PollEntry"%>
<%@page import="java.util.List"%>

<html>

<%
	List<PollOptionEntry> pollOptions = (List<PollOptionEntry>) request
			.getAttribute("pollOptions");
	List<PollEntry> polls = (List<PollEntry>) request
			.getAttribute("polls");
	Integer selectedPoll = (Integer) request.getSession().getAttribute("selectedPoll");

	String title = "";
	String message = "";
	for (PollEntry pe : polls) {
		if (pe.getId() == selectedPoll) {
			title = pe.getTitle();
			message = pe.getMessage();
		}
	} 
%>

<body>

	<h1><%=title%></h1>
	<p><%=message%></p>

	<ol>
			<%
				for (PollOptionEntry poe : pollOptions) {
			%>
			<li><a href="glasanje-glasaj?id=<%=poe.getId()%>"><%=poe.getOptionTitle()%></a></li>
			<%
				}
			%>
	</ol>


</body>

</html>