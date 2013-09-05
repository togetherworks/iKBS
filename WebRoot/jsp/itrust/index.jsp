<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="index.jsp.title"/></title>
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
			<fmt:message key="index.jsp.title"/>
        </td>
        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
      </tr>
      </table>      
      <br>
	  <div class="bodytxt"><fmt:message key="index.jsp.Desc"/></div>
    </td>
  </tr>
</table>
</body>
</fmt>
</html>