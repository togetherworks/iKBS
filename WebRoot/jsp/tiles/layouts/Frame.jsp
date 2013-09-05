<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>

<html:html>
<head>
  <link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">
</head>

<script type="text/javascript" language="Javascript1.1"> 
  var today = new Date();
  var expired = new Date(today.getTime() - 48 * 60 * 60 * 1000); // less 2 days
  var bikky = document.cookie;


function deleteCookie(attribute) {
    document.cookie=attribute + "=null; path=/; expires=" + expired.toGMTString();
    bikky = document.cookie;
}

function dode(){
	deleteCookie("AA_JSessionInfo_Cookie_IP");
	deleteCookie("AA_JSessionInfo_Cookie");
} 
</script>

<frameset rows="85,*,1" frameborder="NO" border="0" framespacing="0" cols="*">
  <frame src="<session:constant name="ContextPath"/>/kbs/mainHeader.do" scrolling="NO" noresize>
  <frame src="<session:constant name="ContextPath"/><tiles:getAsString name="msg_main"/>" name="msg_main" scrolling="auto" noresize>
  <frame src="about:blank" name="menu" scrolling="auto" noresize>
  <noframes>
  <body bgcolor="#FFFFFF" text="#000000">
  </body>
  </noframes>
</frameset>

</html:html>
