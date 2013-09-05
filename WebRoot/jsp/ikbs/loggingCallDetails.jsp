<%@ page language="java"%>
<%@ page import="com.ncs.ikbs.util.*"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<HTML>
	<HEAD>
		<TITLE>Logging Call Details</TITLE>
		<META http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<LINK rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
		<SCRIPT type="text/javascript" src="/ikbs/scripts/kbs.js"></SCRIPT>
	</HEAD>
	<BODY>
		<TABLE align="center" valign="middle">

			<%
String loggingID = request.getParameter("loggingID");

Connection con = null;
Transaction txn = new Transaction();

con = txn.getConnection();
String sel = "SELECT * FROM IKBS_LOGGING_PROC WHERE ID = '" + loggingID + "'";
Statement s = con.createStatement();
ResultSet rs = s.executeQuery(sel);

while(rs.next())
{
%>
			<TR>
				<TD class="tableHeader" width="20">
					Product
				</TD>
				<TD class="tableValue"><%= HTMLTool.cleanString(rs.getString("product"))%>
				</TD>
			</TR>
			<TR>
				<TD class="tableHeader" width="20">
					ProblemType
				</TD>
				<TD class="tableValue"><%= HTMLTool.cleanString(rs.getString("problem_type")) %>
				</TD>
			</TR>
			<TR>
				<TD class="tableHeader" width="20">
					AddInfo
				</TD>
				<TD class="tableValue"><%= HTMLTool.cleanString(rs.getString("addl_info")) %>
				</TD>
			</TR>
			<TR>
				<TD class="tableHeader" width="20">
					MandatoryQuestions
				</TD>
				<TD class="tableValue"><%= HTMLTool.cleanString(rs.getString("mandatory_qn"))%>
				</TD>
			</TR>
			<% }  
rs.close();
con.close();

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

