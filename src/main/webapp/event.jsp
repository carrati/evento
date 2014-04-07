<%@page import="com.evento.bean.Event"%>
<% Event event = (Event)request.getAttribute("event");%>
<%
	if (event.getPicCover() != null) {
%>
	<img alt="<%=event.getName()%>" src="<%=event.getPicCover()%>">
<% } %>
</br>
<%= event.getName() %>
</br>
