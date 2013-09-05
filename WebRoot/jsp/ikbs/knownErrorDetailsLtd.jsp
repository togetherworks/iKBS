<%@ page language="java"%>
<%@ page import = "com.ncs.ikbs.util.*" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ncs.itrust.aa.UserSessionInfo" %>
<%@ page import = "java.util.LinkedList" %>
<%@ page import = "java.util.Iterator" %>
<%@ page import="java.io.Reader" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Known Error Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
<script type="text/javascript" src="/ikbs/scripts/kbs.js"></script>
</head>
<body>
<table  align="center" valign="middle" >

<%
String knownErrorID = request.getParameter("knownErrorID");

Connection con1 = null;
Transaction txn1 = new Transaction();

con1 = txn1.getConnection();
String sel1 = "SELECT app_id FROM IKBS_KNOWN_ERR WHERE ID = '" + knownErrorID + "'";
Statement s1 = con1.createStatement();
ResultSet rs1 = s1.executeQuery(sel1);
String appid="";
while (rs1.next()){
	appid=String.valueOf(rs1.getInt("APP_ID"));
}
UserSessionInfo usi =  (UserSessionInfo)pageContext.getSession().getAttribute("AA_UserSessionInfo");
String user = usi.getLoginId() ;
String ip = usi.getRemoteAddress();

boolean ownTheApp = false;
LinkedList appidList = (LinkedList)pageContext.getSession().getAttribute("appidList");
Iterator iter = appidList.listIterator();
while(iter.hasNext()){
	if(((Integer)iter.next()).toString().equals(appid) ){
		ownTheApp = true;
	}
	
}
rs1.close();
con1.close();

if(ownTheApp==false){
	out.println("<tr><td class=\"tableHeader\">You don't have the access right to Application " + appid + "</td></tr>");
    out.close();
}
else {
	Connection con = null;
	Transaction txn = new Transaction();

	con = txn.getConnection();
	String sel = "SELECT * FROM IKBS_KNOWN_ERR WHERE ID = '" + knownErrorID + "'";
	Statement s = con.createStatement();
	ResultSet rs = s.executeQuery(sel);
while(rs.next())
{
%>	

 
<tr>
<td class="tableHeader"  >Solution</td>
</tr>
<tr>
<td class="tableValue">
<%
Clob solution = rs.getClob("solution");                
// Read CLOB data type
if (solution != null) {
    Reader clobData = solution.getCharacterStream();
    int i = 0;
    while ((i = clobData.read()) > 0)
    {
        out.print((char) i);
    }
}
%>
</td>
</tr>

<% }  
rs.close();
con.close();
}
%>

</table>
<table align="right">
<FORM align="right"> <INPUT align="right" TYPE='BUTTON' VALUE='Close' onClick='window.close()'> </FORM>
</table>
</body>
</html>

