<%@page import="java.io.*, java.util.*" %>
<%@page import="com.ncs.itrust.aa.AuthenticationManager" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<%
	String user = request.getParameter("loginName");
	String oldPass = request.getParameter("oriPassword");
	String newPass = request.getParameter("newPassword");
	try {
		if ((user != null) && (oldPass != null) &&  (newPass != null) ) {
%>
				<html>
				<fmt:setBundle basename='messages-acm'/>
				<head>
				<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
				<title><fmt:message key="changepassword.jsp.title"/></title>
				<link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
				</head>
				<body>
				<table width="98%" align="center" cellpadding="0" cellspacing="0" border="0">
				  <tr>
				    <td> <br>
				      <table cellpadding="0" cellspacing="0" border="0" width="60%" height="40">
				      <tr>
				        <td width="53" height="2"><img src="<session:constant name="ContextPath"/>/images/head1.jpg" width="53" height="40"></td>
				        <td background="<session:constant name="ContextPath"/>/images/headbg.jpg" class="header" height="2" width="591">
							<fmt:message key="changepassword.jsp.title"/>
				        </td>
				        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
				      </tr>
				      </table>      
				      <br>
				    </td>
				  </tr>
				</table>
				
<%			if (AuthenticationManager.changePasswordUponFirstLogin(user, oldPass, newPass)) { %>
				<div class='bodytxt'><fmt:message key='changepassword2.jsp.passwordChanged.Desc'/></div><BR>

<%			}
			else  { %>
				<div class='bodytxt'><fmt:message key='changepassword2.jsp.Desc'/></div><BR>
			<% }
			out.println("<form method=\"post\">");
			out.println("<input type=\"button\" value=\"Close\"");
			out.println("onclick=\"window.close()\">");
			out.println("</form>");
			out.println("</body>");
			out.println("</fmt>");
			out.println("</html>");
		}
	}
	catch (Exception e) {
%>
	<fmt:bundle basename='messages-acm'>
	<fmt:message key='<%= (String) e.getMessage() %>' />
	</fmt:bundle>
<%	} %>       

