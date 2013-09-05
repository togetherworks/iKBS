<%@page  %>
<%@page import="com.ncs.itrust.aa.AuthenticationManager" %>

<%
	String RedirectedURL = null;
	String user = request.getParameter("user").toLowerCase();
	String pass = request.getParameter("pass");
	String bypassConcurrentCheck = request.getParameter("blnBypassConcurrentCheck");
	//String configFileLocation = PageContext.getServletContext().getRealPath("/WEB-INF/classes/jaas.properties");
	//System.setProperty("java.security.auth.login.config", configFileLocation );
	RedirectedURL = AuthenticationManager.getNextURL(user, pass, bypassConcurrentCheck, request, response);
%>
<html>
<body>
<form action="<%= RedirectedURL %>" method="post" name="hiddenform">
<input type=hidden name="loginName" value="<%= user %>">
</form>
<SCRIPT Language="Javascript">
<!--
//alert("aa");
parent.menu.location.reload();
document.hiddenform.submit();
 //-->
</SCRIPT>
</body>
</html>
