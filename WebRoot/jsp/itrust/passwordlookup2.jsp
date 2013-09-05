<%@page %>
<%@page import="com.ncs.itrust.aa.AuthenticationManager" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="passwordlookup.jsp.title"/></title>
<SCRIPT Language="Javascript" src="passwordLookup2.js"></SCRIPT>
<link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
</head>
<body onload="document.form.answer.focus();">


<table width="98%" align="center" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td> <br>
      <table cellpadding="0" cellspacing="0" border="0" width="60%" height="40">
      <tr>
        <td width="53" height="2"><img src="<session:constant name="ContextPath"/>/images/head1.jpg" width="53" height="40"></td>
        <td background="<session:constant name="ContextPath"/>/images/headbg.jpg" class="header" height="2" width="591">
			<fmt:message key="passwordlookup.jsp.title"/>
        </td>
        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
      </tr>
      </table>      
      <br>
    </td>
  </tr>
</table>
<%
	String user = request.getParameter("user");
	boolean isLoginNameValid = AuthenticationManager.isLoginNameValid(user);
	if (isLoginNameValid) {		
%>
		<form method="POST" action="passwordlookup3.jsp" name="form" onsubmit="return dode();">
		<div class="bodytxt"><fmt:message key="passwordlookup2.jsp.Statement1"/></div>
		<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr> 
          <td colspan="3" class="underline" nowrap>&nbsp;</td>
        </tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="passwordlookup2.jsp.Statement2"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<B><%= AuthenticationManager.getChallengeQuestion(user) %></B>
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			<fmt:message key="passwordlookup2.jsp.Desc"/></td>
			<td width="2%" class="search_header">:</td>
			<td width="36%" class="table_row_even">
				<input type='text' name='answer' class="form_field">
			</td>
		</tr>
		<tr><td width="15%" class="search_header" nowrap>
			&nbsp;</td>
			<td width="2%" class="search_header">&nbsp;</td>
			<td width="36%" class="table_row_even">
			<input type=submit value="submit" class="button">
			</td>
		</tr>
		<tr> 
            <td colspan="3" class="underline">&nbsp;</td>
        </tr>
		</table>	
		<input type=hidden name="loginName" value="<%= user %>">
		</form>
<%	}
	else { %>
		<B><div class="bodytxt"><fmt:message key="passwordlookup2.jsp.Statement3"/></div></B>
<%	}
%>
		
</body>
</fmt>
</html>