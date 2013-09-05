<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@page import="java.util.*" %>
<%@page import="com.ncs.itrust.aa.SessionManager" %>
<%@page import="com.ncs.itrust.aa.UserSessionInfo" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>
<html>
<fmt:setBundle basename='messages-acm'/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="securePage.jsp.title"/></title>
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
			<fmt:message key="securePage.jsp.title"/>
        </td>
        <td width="30" height="2"><img src="<session:constant name="ContextPath"/>/images/head2.jpg" height="40"></td>
      </tr>
      </table>      
      <br>
		<div class="bodytxt"><fmt:message key="securePage.jsp.Desc"/></div>
    </td>
  </tr>
</table>

<%
	UserSessionInfo USI = SessionManager.getSessionUSI(request);
	if (USI != null) {
		out.println("Method USI.getUsedId() returns => " + USI.getUserId() + "<BR>");
	
		ArrayList al = (ArrayList) USI.getUserRoles();
		String Output = "";
		for (int i = 0; i < al.size(); i++) {
			Output += (String) al.get(i) + ",";
		}
		if ((Output.length() > 0 ) && (Output.endsWith(",")))
			Output = Output.substring(0, (Output.length()-1) );
		out.println("Method USI.getUserRoles() returns => " + Output + "<BR>");
		out.println("Method USI.doesUserHasThisRole('1') returns => " + USI.doesUserHasThisRole("1") + "<BR>");
		out.println("Method USI.doesUserHasThisRole('9') returns => " + USI.doesUserHasThisRole("9") + "<BR>");
		al = new ArrayList();
		al.add("1");
		al.add("7");
		out.println("Method USI.doesUserHaveAllTheseRole(List) returns => " + USI.doesUserHaveAllTheseRole(al) + "<BR>");
		al.add("9");
		out.println("Method USI.doesUserHaveAllTheseRole(List) returns => " + USI.doesUserHaveAllTheseRole(al) + "<BR>");

		out.println("Is resources <B> '/acm/forbiddenPage.jsp'</B> allowed? [USI.isResourceAllowed('resourcePath')] => " + USI.isResourceAllowed("/acm/forbiddenPage.jsp" ) + "<BR>");
		out.println("Is resources <B> '/acm/jsp/itrust/securePage.jsp' </B> allowed?  [USI.isResourceAllowed('resourcePath')] => " + USI.isResourceAllowed("/acm/jsp/itrust/securePage.jsp" ) + "<BR>");
		out.println("Is resources <B> '/pig.jsp'</B> allowed? [USI.isResourceAllowed('resourcePath')] =>" + USI.isResourceAllowed("/pig.jsp" ) + "<BR>");
	
		al = null;
		al = new ArrayList();
		al.add("/acm/forbiddenPage.jsp");
		al.add("/acm/jsp/itrust/securePage.jsp");
		al.add("/pig.jsp");
		out.println("<BR>[USI.areTheseResourcesAllowed('List')]=> " + "<BR>");
		boolean[] tmpResult = USI.areTheseResourcesAllowed(al);
		for ( int i = 0; i < tmpResult.length; i++) {
			out.println("Status is => " + tmpResult[i] + "<BR>");
		}
	}
	else {
		out.println("USI is null");
	}


%>
</body>
</fmt>
</html>