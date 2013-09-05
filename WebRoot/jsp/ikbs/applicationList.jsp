<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/tld/kbs.tld" prefix="kbs"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<HTML>

	<HEAD>

		<TITLE>KBS Applications</TITLE>

		<META http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">

		<LINK rel="StyleSheet" type="text/css" href="/ikbs/css/kbs.css" />

		<LINK rel="StyleSheet" type="text/css" href="/ikbs/css/dtree.css" />

		<SCRIPT type="text/javascript" src="/ikbs/scripts/dtree.js"></SCRIPT>

		<SCRIPT language=Javascript> 
		function showNoteForProSearch() {
			var dMS = document.getElementById("helpNote");
		    if(document.thisForm.appSearch.options[document.thisForm.appSearch.selectedIndex].value == "Problem") {
				dMS.innerHTML = "Up to 3 words only";
			}
			else {
				dMS.innerHTML = " ";
			}
		}
		</SCRIPT>

	</HEAD>

	<BODY>
		<IMG src="/ikbs/image/applicationHeader.gif" width="100%">

		<FORM name=thisForm method="POST" action="/ikbs/kbs/applicationSearch.do">

			<FONT class="pageText">Search </FONT>
			<BR>

			<SELECT name="appSearch" onchange="showNoteForProSearch()">

				<OPTION value="Problem" selected>
					App & Problem Desc
				</OPTION>

				<OPTION value="Application">
					Application
				</OPTION>

				<OPTION value="Skilled Group Contact">
					Skilled Group Contact
				</OPTION>

				<OPTION value="Skilled Group">
					Skilled Group
				</OPTION>

				<OPTION value="System Manager">
					System Manager
				</OPTION>

				<OPTION value="Latest Applications">
					Latest Applications
				</OPTION>

				<OPTION value="CommandCentre">
					CC KEDB
				</OPTION>

			</SELECT>

			<INPUT type="text" name="searchVal" size=12>
			&nbsp;&nbsp;
			<FONT class="pageText"><A id=helpNote>Up to 3 words only</A> </FONT>
			<BR>
			<INPUT type="submit" value="Search">

		</FORM>

		<kbs:applicationMenu />

		<TABLE width="100%" border="0">
			<TR>
				<TD valign="top">

					<kbs:applicationList />

				</TD>
			</TR>
		</TABLE>
	</BODY>
</HTML>