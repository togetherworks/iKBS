<%@ page language="java"%>
<%@ page import = "com.ncs.ikbs.util.*" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "java.util.*" %>
<%@ page import =  "com.ncs.itrust.aa.UserSessionInfo" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>KBS Applications</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<%
	UserSessionInfo usi =  (UserSessionInfo)pageContext.getSession().getAttribute("AA_UserSessionInfo");
    String subject_id = usi.getUserId();
	//pageContext.getSession().setAttribute("appidList",)

	
	Connection con = null;
	Transaction txn = new Transaction();

	con = txn.getConnection();
	String sel = "SELECT appid FROM KBS_SUBJECT2APPLICATION WHERE SUBJECT_ID = '" + subject_id + "'";
	Statement s = con.createStatement();
	ResultSet rs = s.executeQuery(sel);
	List appidList = new LinkedList() ;
	
	while(rs.next())
	{
		appidList.add(new Integer(rs.getInt("APPID")));
	}
	
	session.setAttribute("appidList",appidList);
	
	%>
<frameset rows="40,*" frameborder="NO" border="0" framespacing="0" cols="*">
  <frame src="/ikbs/kbs/mainNavigation.do" name="navFrame" scrolling="NO">
	<frameset cols="270,*" frameborder="NO" border="0" framespacing="0" rows="*">
			
	<%
	String selRole = "select count(*) count" +
		" from tbl_aa_subject_login l, tbl_aa_subject2group sg, tbl_aa_group g " +
		" where l.subject_id = '" + usi.getUserId() + "' " +
		" and l.subject_id = sg.subject_id " +
		" and sg.group_id = g.group_id and group_name = 'KBS_GROUP_LTD'" ;
	Statement sRole = con.createStatement();
	ResultSet rsRole = sRole.executeQuery(selRole);
	while(rsRole.next()){
		int i = rsRole.getInt("count");
		if(i>=1) {
			%>
			<frame src="/ikbs/kbs/applicationListLtd.do" name="listFrame" scrolling="YES">
	  		<frame src="/ikbs/kbs/applicationDetailsLtd.do" name="detailsFrame" scrolling="YES">
			<%
		}
		else {
			%>
			<frame src="/ikbs/kbs/applicationList.do" name="listFrame" scrolling="YES">
	  		<frame src="/ikbs/kbs/applicationDetails.do" name="detailsFrame" scrolling="YES">
			<%
			
		}
	}
	
	rs.close();
	con.close();
	
	
	%>
			  
	</frameset>
	<noframes>
	<body>
		
	
	</body>
	</noframes>
</frameset>
</html>
