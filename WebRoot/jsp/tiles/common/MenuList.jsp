<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/iextend-menu.tld" prefix="menu"%>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session"%>

<html:html>
<head>
<link rel="stylesheet" type="text/css" media="screen"
        href="<session:constant name="ContextPath"/>/css/menuExpandable.css" />
  <script language="JavaScript1.2" src="<session:constant name="ContextPath"/>/scripts/menuExpandable.js"></script>
</head>
<body>
    <menu:display name="ListMenu" repository="KBS-MENU" permissions="aaAdapter" />
    <br>
    <session:login type="button" loginPath="/ikbs/jsp/itrust/loginForm.jsp" logoutPath="/ikbs/jsp/itrust/logout.jsp" loginText="label.login" logoutText="label.logout" target="msg_main" />
  </body>
</html:html>
