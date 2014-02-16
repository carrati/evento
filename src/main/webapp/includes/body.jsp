<%
	pageContext.setAttribute("page",
		"pages/" + request.getParameter("pageName") + ".jsp");
%>

<div class="site-wrapper">
	<div class="site-wrapper-inner">
		<div class="cover-container">
		
			<jsp:include page="header.jsp"></jsp:include>

			<div class="page_body">
				<jsp:include page="${page}"></jsp:include>
			</div>

			<jsp:include page="footer.jsp"></jsp:include>

		</div>
	</div>
</div>