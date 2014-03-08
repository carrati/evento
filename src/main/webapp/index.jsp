Hello world!!!
<%@page import="com.evento.facebook.FacebookConnect"%>
</br>
<% String s = "tes"; %>
<%= s %>
<%= session.getAttribute("facebookConnect") != null ? ((FacebookConnect)session.getAttribute("facebookConnect")).getAccessToken() : "" %>
</br>
<a href="/fbConnect/doLogin">login</a>