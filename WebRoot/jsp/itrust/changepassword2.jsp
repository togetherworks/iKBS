<%@page  %>
<%@page import="com.ncs.itrust.aa.AuthenticationManager" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<%
	String user = request.getParameter("loginName");
	String oldPass = request.getParameter("oriPassword");
	String newPass = request.getParameter("newPassword");
	try {
		if ((user != null) && (oldPass != null) &&  (newPass != null) ) {
			if (AuthenticationManager.changePasswordUponFirstLogin(user, oldPass, newPass)) {
				String RedirectedURL = AuthenticationManager.getNextURL(user, newPass, request, response); 
%> 				<html><body>
				<form action="<%= RedirectedURL %> " method="post" name="hiddenform">
				
				<input type=hidden name="loginName" value="<%= user %>" class="form_field">
				</form>
				<SCRIPT Language="Javascript">
				<!--
				parent.menu.location.reload();
				document.hiddenform.submit();
				 //-->
				</SCRIPT>
				</body>
				</html>
<%			}
			else  {
%>				<html>
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

				<div class="bodytxt"><fmt:message key="changepassword2.jsp.Desc"/></div><BR>
				<form action="loginForm.jsp" method="POST">
					<input type=submit value="Back to Login Page" class="button">	
				</form>
				</body>
				</fmt>
				</html>
<%			}
		}
	}
	catch (Exception e) {
%>

<font class="red">Error Occured: <%= e.getMessage() %><br><br>

<button class="button" onClick="history.back();">Back</button>

<%	} %>       

