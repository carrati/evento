<%
	String active = "class=\"active\"",
			pageName = request.getParameter("pageName");

%>
<header class="masthead clearfix">
	<div class="inner">
		<h3 class="masthead-brand">VAIBOMBAR</h3>
		<% if ( pageName == null || !"login".equals(pageName) ) {%>
			<ul class="nav masthead-nav">
				<li <%=pageName != null && "dashboard".equals(pageName) ? active : ""%>><a href="/?page=dashboard">Home</a></li>
				<li <%=pageName != null && "rate".equals(pageName) ? active : ""%>><a href="/?page=rate">Avaliar</a></li>
				<li <%=pageName != null && "events".equals(pageName) ? active : ""%>><a href="/?page=events">Eventos</a></li>
			</ul>
		<% } %>
	</div>
</header>
    