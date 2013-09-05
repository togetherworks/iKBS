package com.ncs.ikbs.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ncs.ikbs.util.Transaction;
import com.ncs.itrust.aa.UserSessionInfo;

/** 
 * TODO Provide description 
 * 
 * @author NCS - updated by anthony kartasasmita 
 * @since 23/02/2011 
 */ 
public class ApplicationList extends TagSupport
{
    private static final Logger LOG = Logger.getLogger(ApplicationList.class);
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -1314661406391048439L;

    public int doStartTag() throws JspException
    {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        UserSessionInfo usi =  (UserSessionInfo)pageContext.getSession().getAttribute("AA_UserSessionInfo");
        
        //String subject_id = usi.getUserId();
        //System.out.println("subject_id----"+subject_id);

        String firstLetter = request.getParameter("firstLetter");
        
        System.out.println("ApplicationList.java --- firstLetter---- " + firstLetter);

        if (firstLetter == null)
        {
            firstLetter = "A";
        }

        String sel = "SELECT ID, NAME FROM IKBS_APP WHERE S_NAME LIKE '" + firstLetter.toUpperCase() + "%' ORDER BY NAME";

        //if we want to add application status at end of application name use following query
        //String sel = "SELECT A.ID, A.NAME, G.TITLE  FROM IKBS_APP A, ikbs_gbst G WHERE S_NAME LIKE '" + firstLetter.toUpperCase() + "%' AND a.status2gbst = g.id and g.str_type='STS' ORDER BY NAME";
        
        LOG.debug("App List SQL: " + sel);
        doSearch(pageContext, sel);

        return SKIP_BODY;
    }

    public static void doSearch(PageContext pageContext, String sel) throws JspException
    {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        // Detect browser
        String userAgent = request.getHeader("User-Agent");

        if (userAgent != null)
        {
            userAgent = userAgent.toLowerCase();

            if (userAgent.indexOf("msie") != -1)
            {
                doIESearch(pageContext, sel);
            }
            else
            {
                doNetscapeSearch(pageContext, sel);
            }
        }
    }

    public static void doIESearch(PageContext pageContext, String sel) throws JspException
    {

        Connection con = null;

        ServletRequest request = pageContext.getRequest();
        String searchVal = "";
        if (request.getParameter("searchVal") != null)
            searchVal = request.getParameter("searchVal");
        String appSearch = "";
        if (request.getParameter("appSearch") != null)
            appSearch = request.getParameter("appSearch");

        try
        {
            JspWriter out = pageContext.getOut();

            Transaction txn = new Transaction(true);
            con = txn.getConnection();

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sel);

            if (rs.next())
            {

                // Begin tree
                out.println("<div class=\"dtree\">\n");
                // out.println("<p><a href=\"javascript: d.openAll();\">open
                // all</a> | <a href=\"javascript: d.closeAll();\">close
                // all</a></p>");

                out.println("<script type=\"text/javascript\">");
                out.println("<!--");

                // Write popUp function
                out.println("function popUp(URL) {");
                out.println("  day = new Date();");
                out.println("  id = day.getTime();");
                out.println("  eval(\"page\" + id + \" = window.open(URL, '\" + id + \"', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,width=580,height=380,left = 312,top = 184');\");");
                out.println("}\n");

                out.println("  d = new dTree('d');\n");
                out.println("  d.add(0, -1, 'Results');");

                int index = 0;

                do
                {
                    String appID = rs.getString("ID");
                    String appName = rs.getString("NAME");
                    
                    //if we want to add application status at end of application name
                    //String appStatus = rs.getString("TITLE");
                    

                    // Fix appName for tree
                    appName = appName.replaceAll("'", "&#39;");

                    index++;
                    out.println("  d.add(" + index + ", 0, '" + appName + "');");
                    
                    //if we want to add application status at end of application name
                    //out.println("  d.add(" + index + ", 0, '" + appName + '(' + appStatus +')'+"');");

                    int parent = index;

                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Application Info', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=info', 'Application Info', 'detailsFrame');");
                    index++;
                    if (appSearch.equals("Problem"))
                    {
                        out.println("  d.add(" + index + ", " + parent
                                    + ", 'Matched Logging Calls', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                    + "&searchVal=" + searchVal
                                    + "&type=logMatched', 'Matched Logging Calls', 'detailsFrame');");
                        index++;
                    }
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Logging Calls', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=log', 'Logging Calls', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Skillgroup Support Hours', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=responsibility', 'Skillgroup Support Hours', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Skillgroup Contact', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=contact', 'Skillgroup Contact', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Escalations and Notifications', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=escalation', 'Escalations and Notifications', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'References', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=reference', 'References', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Known Errors', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=technical', 'Known Errors', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'Command Centre', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=commanddb', 'Command Centre', 'detailsFrame');");
                    index++;
                    out.println("  d.add(" + index + ", " + parent
                                + ", 'CC KEDB', '/ikbs/kbs/applicationDetails.do?appid=" + appID
                                + "&type=command', 'CC KEDB', 'detailsFrame');");
                    out.println();

                }
                while (rs.next());

                out.println("  document.write(d);");
                out.println("  d.closeAll();");
                out.println("  //-->");
                out.println("</script>");
                out.println("</div>\n");

            }
            else
            {
                out.println("<font class=\"pageText\">No results.</font>\n");
            }

            rs.close();
            s.close();
            con.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new JspException(e);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);

        }
        finally
        {
            if (con != null)
            {
                try
                {
                    if (!con.isClosed())
                    {
                        con.close();
                    }
                }
                catch (SQLException e)
                {
                    System.err.println("FATAL: Could not close Connection!");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 
     * @param pageContext
     * @param sel
     * @throws JspException
     * @since 23/02/2011
     */
    public static void doNetscapeSearch(PageContext pageContext, String sel) throws JspException
    {

        Connection con = null;

        try
        {
            JspWriter out = pageContext.getOut();

            Transaction txn = new Transaction(true);
            con = txn.getConnection();

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sel);

            if (rs.next())
            {

                // Begin form
                out.println("<form method=POST action='/ikbs/kbs/applicationDetails.do' target='detailsFrame'>\n");

                // Output radio boxes
                do
                {
                    String appID = rs.getString("appID");
                    String appName = rs.getString("appName");

                    // Fix appName for tree
                    appName = appName.replaceAll("'", "&#39;");

                    out.println("<input type='radio' name='appid' value='" + appID + "'><font class='pageText'>"
                                + appName + "</font><br>");

                }
                while (rs.next());

                // Output dropdown menu
                out.println("<select name='type'>");
                out.println("<option value='info'>Application Info</option>");
                out.println("<option value='responsibility'>Skillgroup Support Hours</option>");
                out.println("<option value='contact'>Skillgroup Contact</option>");
                out.println("<option value='escalation'>Escalations and Notifications</option>");
                out.println("<option value='log'>Logging Calls</option>");
                out.println("<option value='reference'>References</option>");
                out.println("<option value='technical'>Technical</option>");
                out.println("<option value='command'>Command</option>");
                out.println("</select>");
                out.println("<br>");
                out.println("<input type='submit' value='Submit'>");
                out.println("</form>");

            }
            else
            {
                out.println("<font class=\"pageText\">No results.</font>\n");
            }

            rs.close();
            s.close();
            con.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new JspException(e);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);

        }
        finally
        {
            if (con != null)
            {
                try
                {
                    if (!con.isClosed())
                    {
                        con.close();
                    }
                }
                catch (SQLException e)
                {
                    System.err.println("FATAL: Could not close Connection!");
                    e.printStackTrace();
                }
            }
        }
    }
}
