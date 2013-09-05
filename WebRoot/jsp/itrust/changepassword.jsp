<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="changepassword.jsp.title"/></title>
<SCRIPT Language="Javascript" src="checkPassword.js"></SCRIPT>
<link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
</head>
<body onload="document.form.oriPassword.focus();"> 
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
    
      <table>
	  <tr><td><div class="bodytxt"><fmt:message key="changepassword.jsp.Desc"/></div></td></tr>
	  <tr><td><div class="bodytxt"><fmt:message key="changepassword.jsp.Reason"/></div></td></tr>
	  <tr><td><div class="bodytxt"><fmt:message key="changepassword.jsp.Reason2"/></div></td></tr>
	  <tr><td><div class="bodytxt"><fmt:message key="changepassword.jsp.Reason3"/></div></td></tr>
	  </table>
    </td>
  </tr>
</table>
	<form method="post" action="changepassword2.jsp" name="form" onsubmit="return dode();">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr> 
          <td colspan="3" class="underline" nowrap>&nbsp;</td>
        </tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="changepassword.jsp.form"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<%= request.getParameter("loginName") %>				
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="changepassword.jsp.form2"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<input type=password name="oriPassword" class="form_field">
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="changepassword.jsp.form3"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<input type=password name="newPassword" class="form_field">
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="changepassword.jsp.form4"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<input type=password name="newPassword2" class="form_field">
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			&nbsp;</td>
			<td width="2%" class="search_header">&nbsp;</td>
			<td width="36%" class="table_row_even">
			<!-- <input type=button value="submit" onclick="dode('changepassword2.jsp')" class="button"> -->
			<input type=submit value="submit" class="button">
			</td>
		</tr>
		<tr> 
            <td colspan="3" class="underline">&nbsp;</td>
        </tr>
	</table>
	<input type=hidden name="loginName" value="<%= request.getParameter("loginName") %>">
	</form>
	</body>
</html>