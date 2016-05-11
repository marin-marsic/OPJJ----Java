<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Random" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<%
	String pickedBgCol = (String) request.getSession().getAttribute(
			"pickedBgCol");
	if (pickedBgCol == null) {
		pickedBgCol = "white";
	}

	StringBuilder strb = new StringBuilder();
	String alphabet = "0123456789abcdef";
	Random random = new Random();
	for (int i = 0; i < 6; i++) {
		strb.append(alphabet.charAt(random.nextInt(alphabet.length())));
	}

	String color = "#" + strb.toString();
%>

<body bgcolor="<%=pickedBgCol%>">

	<p>
		<font color="<%=color%>"> Bob is walking down a country road
			when he spots Farmer Harris standing in the middle of a huge field of
			corn doing absolutely nothing. Bob, curious to find out what's
			happening, walks all the way out to the farmer and asks him, 'Excuse
			me Farmer Harris, could you tell me what you are you doing?' 'I'm
			trying to win a Nobel Prize, 'the farmer replies. 'A Nobel Prize?'
			enquires Bob, puzzled. 'How?' 'Well, I heard they give the Nobel
			Prize to people who are out standing in their field.'</font>
	</p>

	<p>
		<a href="http://localhost:8080/aplikacija2">Return</a>
	</p>
</body>

</html>