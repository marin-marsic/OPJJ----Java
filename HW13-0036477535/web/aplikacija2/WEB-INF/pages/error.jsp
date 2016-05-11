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

	<h1>Error</h1>

	<p>
		Given arguments are invalid.
	</p>
</body>

</html>