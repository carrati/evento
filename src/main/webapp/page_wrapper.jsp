<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	String pageName = "login";
	pageContext.setAttribute("pageName", pageName);
%>
<!DOCTYPE html>
<html lang="pt-BR" class="${pageName}">
	<jsp:include page="includes/head.jsp">
		<jsp:param value="${pageName}" name="pageName"/>
	</jsp:include>
	<body>
		<jsp:include page="includes/body.jsp">
			<jsp:param value="${pageName}" name="pageName"/>
		</jsp:include>
	</body>
</html>