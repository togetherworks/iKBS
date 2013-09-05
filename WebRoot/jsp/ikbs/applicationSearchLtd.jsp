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
	var dMS1 = document.getElementById("initHelpNote");
    if(document.thisForm.appSearch.options[document.thisForm.appSearch.selectedIndex].value == "Problem") {
		dMS.innerHTML = "Up to 3 words only";
		dMS1.innerHTML="";
	}
	else {
		dMS.innerHTML = " ";
		dMS1.innerHTML="";
	}

}
</SCRIPT>
	</HEAD>

	<BODY>



		<IMG src="/ikbs/image/applicationHeader.gif" width="100%" />


		<%
		            String appSearch = request.getParameter("appSearch");

		            String appSel = "";

		            String conSel = "";

		            String grpSel = "";

		            String sysSel = "";

		            String proSel = "selected";

		            String top10Sel = "";

		            String latestSel = "";

		            String ccSel = "";

		            if (appSearch != null && appSearch.equals("System Manager"))
		            {

		                sysSel = "selected";

		                grpSel = "";

		                conSel = "";

		                appSel = "";

		                proSel = "";

		                top10Sel = "";
		                latestSel = "";
		                ccSel = "";

		            }

		            else if (appSearch != null && appSearch.equals("Skillgroup Contact"))
		            {

		                conSel = "selected";

		                grpSel = "";

		                sysSel = "";

		                appSel = "";

		                proSel = "";

		                top10Sel = "";
		                latestSel = "";
		                ccSel = "";

		            }

		            else if (appSearch != null && appSearch.equals("Skillgroup"))
		            {

		                grpSel = "selected";

		                conSel = "";

		                sysSel = "";

		                appSel = "";

		                proSel = "";

		                top10Sel = "";
		                latestSel = "";
		                ccSel = "";

		            }

		            else if (appSearch != null && appSearch.equals("Application"))
		            {

		                proSel = "";

		                conSel = "";

		                sysSel = "";

		                appSel = "selected";

		                grpSel = "";

		                top10Sel = "";
		                latestSel = "";
		                ccSel = "";

		            }
		            else if (appSearch != null && appSearch.equals("Latest Applications"))
		            {

		                proSel = "";

		                conSel = "";

		                sysSel = "";

		                appSel = "";

		                grpSel = "";

		                top10Sel = "";
		                latestSel = "selected";
		                ccSel = "";

		            }
		            else if (appSearch != null && appSearch.equals("CommandCentre"))
		            {

		                proSel = "";

		                conSel = "";

		                sysSel = "";

		                appSel = "";

		                grpSel = "";

		                top10Sel = "";
		                latestSel = "";
		                ccSel = "selected";

		            }
		%>



		<FORM name=thisForm method="POST"
			action="/ikbs/kbs/applicationSearchLtd.do">

			<FONT class="pageText">Search </FONT>
			<BR>

			<SELECT name="appSearch" onchange="showNoteForProSearch()">

				<OPTION value="Problem" <%= proSel %>>
					App & Problem Desc
				</OPTION>

				<OPTION value="Application" <%=appSel%>>
					Application
				</OPTION>

				<OPTION value="Skillgroup Contact" <%=conSel%>>
					Skillgroup Contact
				</OPTION>

				<OPTION value="Skillgroup" <%=grpSel%>>
					Skillgroup
				</OPTION>

				<OPTION value="System Manager" <%=sysSel%>>
					System Manager
				</OPTION>

				<OPTION value="Latest Applications" <%=latestSel%>>
					Latest Applications
				</OPTION>

				<OPTION value="CommandCentre" <%=ccSel%>>
					CC KEDB
				</OPTION>

			</SELECT>

			<INPUT type="text" name="searchVal"
				value="<%=request.getParameter("searchVal")%>" size=12>
			&nbsp;&nbsp;
			<%
			            if (proSel.equals("selected"))
			            {
			%>
			<FONT class="pageText"><A id="initHelpNote">Up to 3 words
					only</A> </FONT>
			<%
			}
			%>
			<FONT class="pageText"><A id="helpNote"> </A> </FONT>

			<BR>

			<INPUT type="submit" value="Search">

		</FORM>



		<kbs:applicationMenuLtd />



		<TABLE width="100%" border="0">

			<TR>

				<TD valign="top">

					<kbs:applicationSearchLtd />

				</TD>

			</TR>

		</TABLE>

	</BODY>

</HTML>