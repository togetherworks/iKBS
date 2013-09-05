<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>

<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="purgeOutError.jsp.title"/></title>
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
			<fmt:message key="purgeOutError.jsp.title"/>
        </td>
        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
      </tr>
      </table>      
      <table>
		<tr><td><div class="bodytxt"><fmt:message key="purgeOutError.jsp.DescPart1"/></div></td></tr>
		<tr><td><div class="bodytxt"><fmt:message key="purgeOutError.jsp.DescPart2"/></div></td></tr>
		<tr><td><div class="bodytxt"><fmt:message key="purgeOutError.jsp.DescPart3"/><div></td></tr>
		<tr><td><div class="bodytxt"><fmt:message key="purgeOutError.jsp.DescPart4"/></div></td></tr>
      </table>
    </td>
  </tr>
</table>

<form method="POST" action="loginForm.jsp" name="form">
	<div class="bodytxt"><fmt:message key="purgeOutError.jsp.Login.Desc"/>
	<input type="submit" value="<fmt:message key="label.login"/>" class="button">
	<input type=hidden name="blnBypassConcurrentCheck" value="1">
</form>

</body>
</fmt>
</html>