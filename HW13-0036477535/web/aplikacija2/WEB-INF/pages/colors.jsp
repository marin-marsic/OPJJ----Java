<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<%
String pickedBgCol = (String)request.getSession().getAttribute("pickedBgCol"); 
if(pickedBgCol == null) {
	pickedBgCol = "white";
}
%>

<body bgcolor="<%=pickedBgCol%>">

<h1>Please choose from one of these colors:</h1>

<p><a href="http://localhost:8080/aplikacija2/setcolor?color=white">WHITE</a></p>
<p><a href="http://localhost:8080/aplikacija2/setcolor?color=red">RED</a></p>
<p><a href="http://localhost:8080/aplikacija2/setcolor?color=green">GREEN</a></p>
<p><a href="http://localhost:8080/aplikacija2/setcolor?color=cyan">CYAN</a></p>
</body>

</html>