<%@ page language="java"%>
<%@ page import="com.ncs.ikbs.util.*"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.Reader"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
	<HEAD>
		<TITLE>Known Error Details</TITLE>
		<META http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<LINK rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
		<SCRIPT type="text/javascript" src="/ikbs/scripts/kbs.js"></SCRIPT>
	</HEAD>
	<BODY>
		<TABLE align="center" valign="middle">

			<%
			try {
String knownErrorID = request.getParameter("knownErrorID");

Connection con = null;
Transaction txn = new Transaction();

con = txn.getConnection();
String sel = "SELECT * FROM IKBS_KNOWN_ERR WHERE ID = '" + knownErrorID + "'";
Statement s = con.createStatement();
ResultSet rs = s.executeQuery(sel);

while(rs.next())
{ 
%>


			<TR>
				<TD class="tableHeader">
					Solution
				</TD>
			</TR>
			<TR>
				<TD class="tableValue">
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
				</TD>
			</TR>

<% }  
rs.close();
con.close();
} 
catch (Exception  e) 
{
    e.printStackTrace(new java.io.PrintWriter(out));
}
%>

		</TABLE>
		<TABLE align="right">
			<FORM align="right">
				<INPUT align="right" TYPE='BUTTON' VALUE='Close'
					onClick='window.close()'>
			</FORM>
		</TABLE>
	</BODY>
</HTML>

