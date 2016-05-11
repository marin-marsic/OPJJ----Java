<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.hw14.model.PollEntry"%>
<%@page import="java.util.List"%>
<%
  List<PollEntry> polls = (List<PollEntry>)request.getAttribute("polls");
%>
<html>
  <body>

  <h1>Dostupne ankete:</h1><br>

  <% if(!polls.isEmpty()) { %>
  
    <% for(PollEntry u : polls) { %>
    <p>
		<a href="http://localhost:8080/aplikacija5/glasanje?pollID=<%= u.getId() %>"><%= u.getTitle() %></a>
	</p>
    <% } %>  
  <% } %>

  </body>
</html>
