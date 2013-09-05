<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/kbs.tld" prefix="kbs" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title>KBS Solutions</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />
	<link rel="StyleSheet" type="text/css" href="/ikbs/css/dtree.css" />
  <script type="text/javascript" src="/ikbs/scripts/dtree.js"></script>
</head>
<body>

<img src="/ikbs/image/solutionHeader.gif"><br>

	<form method="POST" action="/ikbs/kbs/solutionSearch.do">
	<font class="pageText">Search By Keyword:</font><br>
	<input type="text" name="searchVal"><br>
	<input type="submit" value="Search">
	</form>

		<table width="100%" border="0">
			<tr>
				<td valign="top">
					  <kbs:solutionList />
				</td>
			</tr>
		</table>
</body>
</html>