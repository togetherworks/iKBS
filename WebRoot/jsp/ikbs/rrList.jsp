<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/tld/kbs.tld" prefix="kbs" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<html>

	<head>

	<title>Roles & Responsibility</title>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

  <link rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />

  <link rel="StyleSheet" type="text/css" href="/ikbs/css/dtree.css" />

  <script type="text/javascript" src="/ikbs/scripts/dtree.js"></script>

	</head>

	<body>

	

	<img src="/ikbs/image/rrHeader.gif"><br>

	

	<form method="POST" action="/ikbs/kbs/rrSearch.do">

	<font class="pageText">Search </font>

	<table>

	<tr>

		<td class="formName">Node Name:</td>

		<td class="formName"><input type="text" name="nodeName" value="A"></td>

	</tr>

	<tr>

		<td class="formName">Application Name:</td>

		<td class="formName"><input type="text" name="appName"></td>

	</tr>

	<tr>

		<td class="formName">LOB:</td>

		<td class="formName"><kbs:lobSelect /></td>

	</tr>

	<tr>

		<td colspan="2"><input type="submit" value="Search"></td>

	</tr>

	</table>

	</form>

	

		<table width="100%" border="0">

			<tr>

				<td valign="top">

            <kbs:rrList />

				</td>

			</tr>

		</table>

	</body>

</html>