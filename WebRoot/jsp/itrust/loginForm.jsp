<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="loginForm.jsp.title"/></title>
<link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" onload="document.form.user.focus();">
<form method="post" action="loginForm2.jsp" name="form">
<table width="545" border="0" cellspacing="0" cellpadding="0" height="378" align="center">
  <tr>
    <td width="545" height="378" background="<session:constant name="ContextPath"/>/images/splashpix.jpg">
      <table width="50%" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr> 
          <td width="16%"> 
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
          <td width="6%">&nbsp;</td>
          <td width="78%">&nbsp;</td>
        </tr>
        <tr> 
          <td width="16%" class="bodytxt"><fmt:message key="loginForm.jsp.label.loginID"/></td>
          <td width="6%">:</td>
          <td width="78%"> 
			<input type=text name="user"  class="form_field" size="35" maxlength="35">
          </td>
        </tr>
        <tr> 
          <td width="16%" class="bodytxt"><fmt:message key="loginForm.jsp.label.password"/></td>
          <td width="6%">:</td>
          <td width="78%"> 
			<input type="password" name="pass"  class="form_field" size="35" maxlength="35">
          </td>
        </tr>
        <tr> 
          <td colspan="3"><img src="<session:constant name="ContextPath"/>/images/blank.gif" width="5" height="5"></td>
        </tr>
        <tr> 
          <td colspan="2"><A href="passwordlookup.jsp" class="txt_link"> Forget Password?</a></td>
          <td width="78%"> 
            <div align="right">
				<input type="image" src="<session:constant name="ContextPath"/>/images/but_login.gif" value="Submit" alt="Submit" style="cursor:hand">
           	</div>
          </td>
        </tr>
        <tr> 
          <td width="16%">&nbsp;</td>
          <td width="6%">&nbsp;</td>
          <td width="78%">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type=hidden name="blnBypassConcurrentCheck" value="<%= request.getParameter("blnBypassConcurrentCheck") %>">
</form>
</body>
</fmt>
</html>
