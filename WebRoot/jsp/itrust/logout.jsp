<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>

<head>
<fmt:setBundle basename='messages-acm'/>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="logout.jsp.title"/></title>
<link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<table width="545" border="0" cellspacing="0" cellpadding="0" height="378" align="center">
  <tr>
    <td width="545" height="378" background="<session:constant name="ContextPath"/>/images/splashpix.jpg"> 
      <table width="70%" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr>
          <td>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p align="center" class="header">&nbsp;</p>
            <p align="center" class="header">&nbsp;</p>
            <p>&nbsp;</p>
			<p align="center"><span class="bodytxt"><fmt:message key="logout.jsp.Desc"/> 
              <br>	
              <fmt:message key="logout.jsp.ThankYou"/>&nbsp;<b><fmt:message key="logout.jsp.iFrame"/></b>.</span>
              <span class="bodytxt"><br>
			  <fmt:message key="logout.jsp.OfService"/><br>
              </span></p>
            <p align="center">
            	<span class="bodytxt">
				<fmt:message key="logout.jsp.Relogin"/></span>
				<A href="loginForm.jsp" class="txt_link">
				<fmt:message key="logout.jsp.Relogin2"/></a></p>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<SCRIPT Language="Javascript">
<!--
parent.menu.location.reload();
 //-->
</SCRIPT>
</body>
</fmt>
</html>
