<%@page import="com.evento.bean.Event"%>
<% Event event = (Event)request.getAttribute("event");%>
<% if (event.getCover() != null) { %>
	<img alt="<%= event.getName() %>" src="<%=event.getCover()%>">
<% } %>
</br>
<%= event.getName() %>
</br>
