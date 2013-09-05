<%@ page language="java" %>
<%@ page import = "com.ncs.ikbs.util.*" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "java.util.*" %>
<%@ page import =  "com.ncs.itrust.aa.UserSessionInfo" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>KBS</title>
		<link rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
	</head>
	
	
	
	<body>
	
	<%
	UserSessionInfo usi =  (UserSessionInfo)pageContext.getSession().getAttribute("AA_UserSessionInfo");
    String subject_id = usi.getUserId();
	//pageContext.getSession().setAttribute("appidList",)

	
	Connection con = null;
	Transaction txn = new Transaction();

	con = txn.getConnection();
	String sel = "select count(*) count" +
	" from tbl_aa_subject_login l, tbl_aa_subject2group sg, tbl_aa_group g " +
	" where l.subject_id = '" + usi.getUserId() + "' " +
	" and l.subject_id = sg.subject_id " +
	" and sg.group_id = g.group_id and group_name = 'KBS_GROUP_LTD'" ;
	Statement s = con.createStatement();
	ResultSet rs = s.executeQuery(sel);
	
	while(rs.next()){
		int i = rs.getInt("count");
		if(i>=1) {
			%>
			<a class="alphaMenu" href="<session:constant name="ContextPath"/>/kbs/applicationListLtd.do" target="listFrame"><img src="/ikbs/image/worldButton.gif" border="0"> Applications</a>
  &nbsp;&nbsp;&nbsp;
			<%
		}
		else {
			%>
			<a class="alphaMenu" href="<session:constant name="ContextPath"/>/kbs/applicationList.do" target="listFrame"><img src="/ikbs/image/worldButton.gif" border="0"> Applications</a>
  &nbsp;&nbsp;&nbsp;
  			<%
			
		}
	}
	
	rs.close();
	con.close();
	
	
	%>
	
	
	<!--  <a class="alphaMenu" href="<session:constant name="ContextPath"/>/kbs/solutionList.do" target="listFrame"><img src="/ikbs/image/worldButton.gif" border="0"> Solutions</a> 
	&nbsp;&nbsp;&nbsp; -->
	<!-- <a class="alphaMenu" href="<session:constant name="ContextPath"/>/kbs/rrList.do" target="listFrame"><img src="/ikbs/image/worldButton.gif" border="0"> Roles & Responsibilities</a>
	&nbsp;&nbsp;&nbsp;  -->
	<a class="alphaMenu" href="/kbs/img/techref/Clarify Severity Matrix.doc"  target= "_blank"><img src="/ikbs/image/worldButton.gif" border="0">Severity Matrix</a>
	&nbsp;&nbsp;&nbsp;
  	<a class="alphaMenu" href="<session:constant name="ContextPath"/>/jsp/itrust/userchangepassword.jsp" target="="_blank"><img src="/ikbs/image/worldButton.gif" border="0"> Change Password</a>
	&nbsp;&nbsp;&nbsp;
  	<a class="alphaMenu" href="<session:constant name="ContextPath"/>/jsp/itrust/logout.jsp" target="msg_main"><img src="/ikbs/image/worldButton.gif" border="0"> Logout</a>


	</body>
</html>
