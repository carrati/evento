<%
	String pageName = request.getParameter("pageName");
	pageContext.setAttribute("pageName", pageName);
	pageContext.setAttribute("pagePath", "pages/" + pageName + ".jsp");
%>

<div class="site-wrapper">
	<div class="site-wrapper-inner">
		<div class="cover-container">
		
			<jsp:include page="header.jsp">
				<jsp:param value="${pageName}" name="pageName"/>
			</jsp:include>

			<div class="page_body">
				<jsp:include page="${pagePath}"></jsp:include>
			</div>

			<jsp:include page="footer.jsp"></jsp:include>

		</div>
	</div>
</div>