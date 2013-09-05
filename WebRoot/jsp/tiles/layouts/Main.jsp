<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean-el.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>

<tiles:useAttribute name="title"/>

<html:html>
<head>
  <title><bean:message name='title'/></title>
  <link rel="stylesheet" href="<session:constant name="ContextPath"/>/css/site.css" type="text/css">

  <SCRIPT LANGUAGE = "JavaScript">
  <!-- Refreshes the window tile
  function setWindowTitle() {
    top.document.title="<bean:message name='title'/>";
  }
  // -->
  </script>
</head>
<body onLoad="setWindowTitle()"  text="#000000" link="#336699" vlink="#009999" alink="#990000">
<table width="98%" align="center" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td> <br>
      <table cellpadding="0" cellspacing="0" border="0" width="540" height="40">
      <tr>
        <td width="53" height="2"><img src="<session:constant name="ContextPath"/>/images/head1.jpg" width="53" height="40"></td>
        <td background="<session:constant name="ContextPath"/>/images/headbg.jpg" class="header" height="2" width="591">
          <bean:message name='title'/>
        </td>
        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
      </tr>
      </table>
      <tiles:get name="messages"/>
      <br>
      <tiles:get name="main"/>
    </td>
  </tr>
</table>
</body>
</html:html>
