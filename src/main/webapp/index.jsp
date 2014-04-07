Hello world!!!
<%@page import="java.util.Map"%>
<%@page import="com.evento.facebook.FacebookConnect"%>
</br>
<% FacebookConnect fb =  session.getAttribute("facebookConnect") != null ? ((FacebookConnect)session.getAttribute("facebookConnect")) : null;%>
<% if (fb != null) {%>
	<%=  fb.getAccessToken() %>
	<% for (Map.Entry<String, Object> entry : fb.getEvents().entrySet()) { %>
			<a href="/event/<%= ((Map<String, Object>)entry.getValue()).get("eid") %>"><%= ((Map<String, Object>)entry.getValue()).get("name") %></a><br/><br/><br/>
			-------------------------------------------------------------------
			<br/>
	<% } %>
<% } %>
</br>
<a href="/fbConnect/doLogin">login</a>