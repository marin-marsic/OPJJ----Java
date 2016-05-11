<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>

<%
	String pickedBgCol = (String) request.getSession().getAttribute(
			"pickedBgCol");
	if (pickedBgCol == null) {
		pickedBgCol = "white";
	}
%>

<body bgcolor="<%=pickedBgCol%>">

	<h1>Table of trigonometric values:</h1>

	<p>
		<a href="http://localhost:8080/aplikacija2">Return</a>
	</p>

	<table>
		<thead>
			<tr>
				<th>Degree</th>
				<th>Sin</th>
				<th>Cos</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="value" items="${results }">
				<tr>
					<th>${value.degree }</th>
					<th><fmt:formatNumber type="number" maxFractionDigits="5"
							value="${value.sin}" /></th>
					<th><fmt:formatNumber type="number" maxFractionDigits="5"
							value="${value.cos}" /></th>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>

</html>