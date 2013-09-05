<%@ page language="java"%>
<%@ page import = "com.ncs.ikbs.util.*" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.sql.*" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Command Centre Note Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
<script type="text/javascript" src="/ikbs/scripts/kbs.js"></script>
</head>
<body>
<table  align="center" valign="middle" >
<%
String ccNoteId = request.getParameter("ccNoteId");

Connection con = null;
Transaction txn = new Transaction();

con = txn.getConnection();
String sel = "select c.*, g.NAME as groupName from IKBS_CC_FIX c, IKBS_CC_FIX_SKG cg, IKBS_SKG G " +
		"WHERE c.APP_ID = cg.APP_ID AND cg.SKG_ID = g.ID AND " +
		"c.ID = '" + ccNoteId + "'";
Statement s = con.createStatement();
ResultSet rs = s.executeQuery(sel);


if(rs.next())
{
%>	
<tr>
<td class="tableHeader" width="10%">SYMPTOMS</td>
<td class="tableValue"><%= HTMLTool.cleanString(rs.getString("symptom"))%> 
</td>
</tr>

<tr>
<td class="tableHeader" width="10%">SKILLGROUP</td>
<td class="tableValue"><%= HTMLTool.cleanString(rs.getString("groupName")) %> 
</td>
</tr> 

<tr>
<td class="tableHeader" width="10%">RCACONCLUSION</td>
<td class="tableValue"><%= HTMLTool.cleanString(rs.getString("rca_conclusion"))%> 
</td>
</tr>

<tr>
<td class="tableHeader" width="10%">FIX</td>
<td class="tableValue"><%= HTMLTool.cleanString(rs.getString("fix")) %> 
</td>
</tr> 

<% }  
rs.close();
con.close();

%>

</table>
<table align="right">
<FORM align="right"> <INPUT align="right" TYPE='BUTTON' VALUE='Close' onClick='window.close()'> </FORM>
</table>
</body>
</html>

