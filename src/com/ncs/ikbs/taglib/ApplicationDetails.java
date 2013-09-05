package com.ncs.ikbs.taglib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ncs.ikbs.util.HTMLTool;
import com.ncs.ikbs.util.Transaction;
import com.ncs.itrust.aa.UserSessionInfo;


/**
 * Custom Tag to display related Application details such as
 * application info, logging calls, known errors, 
 * Skill Group contacts etc.
 * 
 * @author NCS, updated by anthony kartasasmita 
 * @since 25/02/2011
 */
public class ApplicationDetails extends TagSupport
{
    public static final Logger LOG = Logger.getLogger(ApplicationDetails.class);

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2359113242860497054L;
    private String role = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
    
    public int doStartTag() throws JspException
    {

        Connection con = null;

        try
        {
            ServletRequest request = pageContext.getRequest();

            String type = request.getParameter("type");

            if (type != null && !type.equals(""))
            {
                String appid = request.getParameter("appid");

                if (appid == null || appid.equals(""))
                {
                    throw new JspException("Require value for parameter appid");
                }

                int appidInt = 0;

                try
                {
                    appidInt = Integer.parseInt(appid);
                }
                catch (NumberFormatException e)
                {
                    throw new JspException("Application ID not a number");
                }

                Transaction txn = new Transaction();
                con = txn.getConnection();
                openLogFile("kbsAppVisit.log"); /*  */
                // UserId = iTrust_USI.getUserId();

                UserSessionInfo usi = (UserSessionInfo) pageContext.getSession().getAttribute("AA_UserSessionInfo");
                String user = usi.getLoginId();
                String ip = usi.getRemoteAddress();

                Iterator iter = usi.getUserRoleNames().iterator();
                while (iter.hasNext())
                {
                    role += iter.next().toString();
                }

                writeToLogFile("|" + user + "|" + ip + "|" + type + "|" + appid);
                closeLogFile();

                if (type.equals("info"))
                {
                    doInfo(con, appidInt);

                }
                else if (type.equals("responsibility"))
                {
                    doSkillgourpSupportHrs(con, appidInt);

                }
                else if (type.equals("contact"))
                {
                    doSkillgroupContact(con, appidInt);

                }
                else if (type.equals("escalation"))
                {
                    doEscalation(con, appidInt);

                }
                else if (type.equals("log"))
                {
                    doLoggingCall(con, appidInt);

                }
                else if (type.equals("logMatched"))
                {
                    doMatchedLog(con, appidInt);

                }
                else if (type.equals("reference"))
                {
                    doReference(con, appidInt);

                }
                else if (type.equals("technical"))
                {
                    doKnownError(con, appidInt);

                }
                else if (type.equals("command"))
                {
                    doCommandCenterKEDB(con, appidInt);

                }
                else if (type.equals("commanddb"))
                {
                    doCommandCenter(con, appidInt);

                }
                else if (type.equals("image"))
                {
                    doImage(con, appidInt);

                }
                else
                {
                    throw new JspException("Unknown value for parameter type");
                }

            }

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

        return SKIP_BODY;
    }

    /**
     * Display Application Info
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 25/02/2011
     */
    protected void doInfo(Connection con, int appid) throws SQLException, IOException
    {
        String sel = "SELECT  a.ID, a.NAME, a.LONG_NAME, a.ALIAS, a.URL, a.DESCRIPTION, " +
        		"(select TITLE from IKBS_GBST cri where cri.id = a.CRITICALITY2GBST) as CRITICALITY, " +
        		"(select TITLE from IKBS_GBST cri where cri.id = a.STATUS2GBST) as STATUS, " +
        		"(select FIRST_NAME || ' ' || LAST_NAME from IKBS_CONTACT con where con.id = a.SYS_MGR) as SYSMGR, " +
        		"(select FIRST_NAME || ' ' || LAST_NAME from IKBS_CONTACT con where con.id = a.BUSINESS_OWNER) as BUSINESSOWNER, " +
        		"(select FIRST_NAME || ' ' || LAST_NAME from IKBS_CONTACT con where con.id = a.PC) as PC, " +
        		"a.TRAINING_DOC, a.DETAIL, a.DT_UPD, a.UPD_BY FROM IKBS_APP a WHERE a.ID = '" + appid + "'";
        
        LOG.debug("App Info SQL: " + sel);
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();
       
        while (rs.next())
        {
            StringBuffer appName = new StringBuffer(HTMLTool.cleanString(rs.getString("NAME")));
            // Append application long name to application name if it has a data
            String appLongName = rs.getString("LONG_NAME");
            if (appLongName != null && appLongName.length() > 0) 
            {
                appName = appName.append(" (").append(appLongName).append(")");                                         
            }
            
            String appAlias = HTMLTool.cleanString(rs.getString("ALIAS"));
            String appUrl = HTMLTool.cleanString(rs.getString("URL"));            
            String appBrief = HTMLTool.cleanString(rs.getString("DESCRIPTION"));
            
            String appSeverity = HTMLTool.cleanString(rs.getString("CRITICALITY"));
            String appStatus = HTMLTool.cleanString(rs.getString("STATUS"));
            String sysManager = HTMLTool.cleanString(rs.getString("SYSMGR"));
            String businessOwner = HTMLTool.cleanString(rs.getString("BUSINESSOWNER"));
            String productionCoordinator = HTMLTool.cleanString(rs.getString("PC"));
            
            String trianingDocuments = HTMLTool.cleanString(rs.getString("TRAINING_DOC"));
            Clob appDetails = rs.getClob("DETAIL");            

            String modifiedDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
            	 
                modifiedDate = dateFormat.format(rs.getDate("DT_UPD"));
                System.out.print("modifiedDate---after format" +modifiedDate);
            }
            
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));

            out.print("<p class=\"tableHeader\">Application</p>");
            out.print("<tr><td class=\"tableHeader\">Name:</td><td class=\"tableValue\">" + appName
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">Alias:</td><td class=\"tableValue\">" + appAlias
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">URL:</td><td class=\"tableValue\">" + appUrl
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">Description:</td><td class=\"tableValue\">" + appBrief
                      + "</td>");

            out.print("<tr><td class=\"tableHeader\">Criticality:</td><td class=\"tableValue\">" + appSeverity + "</td>");
            out.print("<tr><td class=\"tableHeader\">Status:</td><td class=\"tableValue\">" + appStatus + "</td>");
            out.print("<tr><td class=\"tableHeader\">System Manager:</td><td class=\"tableValue\">" + sysManager
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">Business Owner:</td><td class=\"tableValue\">" + businessOwner
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">Production Coordinator:</td><td class=\"tableValue\">"
                      + productionCoordinator + "</td>");
            
            out.print("<tr><td class=\"tableHeader\">Training Document:</td><td class=\"tableValue\">"
                      + trianingDocuments + "</td>");

            out.print("<tr><td class=\"tableHeader\">Details:</td><td class=\"tableValue\">");
            
            // Read CLOB data type
            if (appDetails != null) {
                Reader clobData = appDetails.getCharacterStream();
                int i = 0;
                while ((i = clobData.read()) > 0)
                {
                    out.print((char) i);
                }
            }
                      
            out.print("</td>");

            out.print("<tr><td class=\"tableHeader\">Last Modified:</td><td class=\"tableValue\">" + modifiedDate
                      + "</td>");
            out.print("<tr><td class=\"tableHeader\">Updated By:</td><td class=\"tableValue\">" + updatedBy
                      + "</td>");

        }

        rs.close();
        s.close();
    }

    /**
     * Display the Skill Support Hours
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 25/02/2011
     */
    protected void doSkillgourpSupportHrs(Connection con, int appid) throws SQLException, IOException
    {

        String sel = "SELECT g.NAME as groupName, ag.RESPONSIBILITY, " +
        		"(select TITLE from IKBS_GBST whr where whr.id = ag.SUPP_HR2GBST) as supportHour, " +
        		"(select TITLE from IKBS_GBST whr where whr.id = ag.SUPP_COND2GBST) as supportCondition, " +
        		"ag.DT_UPD as modifiedDate, ag.UPD_BY as updatedBy " +
        		"FROM IKBS_APP_SKG ag, IKBS_SKG g WHERE ag.SKG_ID = g.ID AND " +
        		"ag.APP_ID = '" + appid + "'"+
                "order by g.name, supportCondition, supportHour";
        
        LOG.debug("Group SQL: " + sel);
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"120\">Skill Group Name</td>");
        out.print("<td class=\"tableHeader\" width=\"120\">Support Condition</td>");
        out.print("<td class=\"tableHeader\" width=\"150\">Support Hours</td>");
        out.print("<td class=\"tableHeader\" width=\"310\">Area of Responsibility</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Modified Date</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Updated By</td></tr>");
        while (rs.next())
        {
            String groupName = HTMLTool.cleanString(rs.getString("groupName"));
            String groupSupportCondition = HTMLTool.cleanString(rs.getString("supportCondition"));
            String groupSupportHour = HTMLTool.cleanString(rs.getString("supportHour"));
            String groupResponsibility = HTMLTool.cleanString(rs.getString("Responsibility"));
            String updatedBy = HTMLTool.cleanString(rs.getString("updatedBy"));
            String groupModifiedDate = "";
            if (rs.getDate("modifiedDate") != null)
            {
                groupModifiedDate = dateFormat.format(rs.getDate("modifiedDate"));
            }

            out.print("<tr><td class=\"tableValue\">" + groupName + "</td>" +
                          "<td class=\"tableValue\">" + groupSupportCondition + "</td>" +
                          "<td class=\"tableValue\">" + groupSupportHour + "</td>" +
                          "<td class=\"tableValue\">" +  groupResponsibility+ "</td>" +
                          "<td class=\"tableValue\">" + groupModifiedDate + "</td>" +
                          "<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }

        rs.close();
        s.close();
    }

    /**
     * Display Skill Group Contact
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 26/05/2011
     * @author cheng zhang
     */
    protected void doSkillgroupContact(Connection con, int appid) throws SQLException, IOException
    {

        String psel = "SELECT g.id as groupId,g.NAME as groupName, ac.CONTACT_TYPE, c.first_name, c.last_name,c.phone,c.mobile ,c.email, " +
        		"ac.DT_UPD as modifiedDate, ac.UPD_BY as updatedBy " +
        		"FROM IKBS_APP_CONTACT ac, IKBS_CONTACT c, IKBS_SKG g WHERE " +
        		"ac.CONTACT_ID = c.ID AND ac.SKG_ID = g.ID AND " +
        		"ac.APP_ID = '" + appid + "' " +
        		"and ac.contact_type='P' " +
        		"order by groupName, ac.CONTACT_TYPE";

        LOG.debug("Contact SQL: " + psel);

        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(psel);  
       
        JspWriter out = pageContext.getOut();

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"190\">Skill Group Name</td>");
        out.print("<td class=\"tableHeader\" width=\"200\">Primary Contact</td>");
        out.print("<td class=\"tableHeader\" width=\"200\">Secondary Contact</td>");
        out.print("<td class=\"tableHeader\" width=\"100\">Modified Date</td>");
        out.print("<td class=\"tableHeader\" width=\"100\">Updated By</td></tr>");

        
       
        while (rs.next())
        {
            String groupId = HTMLTool.cleanString(rs.getString("groupId"));
            
            String contactGroupName = HTMLTool.cleanString(rs.getString("groupName"));
            String contactName = HTMLTool.cleanString(rs.getString("First_Name")) + " " +
            HTMLTool.cleanString(rs.getString("Last_Name"));
            String contactPhone = HTMLTool.cleanString(rs.getString("Phone"));
            String contactMobile =HTMLTool.cleanString(rs.getString("Mobile"));
            String contactEmail = HTMLTool.cleanString(rs.getString("Email"));
            String pupdatedBy = HTMLTool.cleanString(rs.getString("updatedBy"));
            
            String tmp1;

			Calendar cal = Calendar.getInstance();
			cal.set(1999, 1, 1);
			Date primaryModifiedDate = cal.getTime();
			Date backupModifiedDate = cal.getTime();
			String secUpdatedBy ="";
			
			System.out.println("primaryModifiedDate -- "+ primaryModifiedDate);
			System.out.println("backupModifiedDate -- "+ backupModifiedDate);
                        
            if (rs.getDate("modifiedDate") == null){
            	tmp1 = "";
            }else{
            	primaryModifiedDate = rs.getDate("modifiedDate");
            }
            
            out.print("<tr><td class=\"tableValue\">" + contactGroupName
					+ "</td><td class=\"tableValue\">" + contactName
					+ "<br>Phone: " + contactPhone 
					+ "<br>Mobile: "+ contactMobile 
					+ "<br>Email: " + contactEmail
					+ "</td><td class=\"tableValue\">");
            
            System.out.println("contactGroupName -- "+ contactGroupName);
            //check backup
            String ssel = "SELECT g.id as groupId,g.NAME as groupName, ac.CONTACT_TYPE, c.first_name, c.last_name,c.phone,c.mobile ,c.email, " +
			    		"ac.DT_UPD as modifiedDate, ac.UPD_BY as updatedBy " +
			    		"FROM IKBS_APP_CONTACT ac, IKBS_CONTACT c, IKBS_SKG g WHERE " +
			    		"ac.CONTACT_ID = c.ID AND ac.SKG_ID = g.ID AND " +
			    		"ac.APP_ID = '" + appid + "' " +
			    		"and ac.contact_type='S' " +
			    		"and g.name = '"+contactGroupName +"' " +
			    		"order by groupName, ac.CONTACT_TYPE, ac.disp_seq";
			            
            Statement s2 = con.createStatement();
			ResultSet ts = s2.executeQuery(ssel);
            
			while (ts.next()) {
				String scontactName = HTMLTool.cleanString(ts.getString("First_Name")) + " " +
	            HTMLTool.cleanString(ts.getString("Last_Name"));
	            String scontactPhone = HTMLTool.cleanString(ts.getString("Phone"));
	            String scontactMobile = HTMLTool.cleanString(ts.getString("Mobile"));
	            String scontactEmail = HTMLTool.cleanString(ts.getString("Email"));
	            String supdatedBy = HTMLTool.cleanString(ts.getString("updatedBy"));
	            secUpdatedBy=supdatedBy;
				if (ts.getDate("modifiedDate") != null) {
					if (ts.getDate("modifiedDate").after(backupModifiedDate)) {
						backupModifiedDate = ts.getDate("modifiedDate");
					}
				}
				
				out.print(scontactName + "<br>Phone: " + scontactPhone + "<br>Mobile: "
						+ scontactMobile + "<br>Email: " + scontactEmail + "<br>");
			}
						
			String contactModifiedDateString;
			String contactUpdateBy=null;
			
			
			if (primaryModifiedDate.after(backupModifiedDate)) {
				contactModifiedDateString = HTMLTool.cleanString(dateFormat.format(primaryModifiedDate));
				contactUpdateBy=pupdatedBy;
				
				System.out.println(" primaryModifiedDate.after(secondryModifiedDate) --- contactModifiedDateString -- " + contactModifiedDateString);
			} else {
				contactModifiedDateString = HTMLTool.cleanString(dateFormat.format(backupModifiedDate));
				contactUpdateBy=secUpdatedBy;
				System.out.println(" else --- contactModifiedDateString -- " + contactModifiedDateString);

			}
			if (contactModifiedDateString.indexOf("1999") >= 0)
				contactModifiedDateString = "";
			
			ts.close();
			s2.close();
			
			out.print("</td><td class=\"tableValue\">" + contactModifiedDateString + "</td>");
            out.print("</td><td class=\"tableValue\">" + contactUpdateBy + "</td></tr>");  
			
		}

		rs.close();
		s.close();
            
}

    /**
     * Display Escalations and Notifications
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 28/02/2011
     */
    protected void doEscalation(Connection con, int appid) throws SQLException, IOException
    {
        // Do Mandatory Notification
        String sel = "SELECT * FROM IKBS_MDT_NOTIF WHERE APP_ID = '" + appid + "'";
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();
        rs = s.executeQuery(sel);

        out.print("<tr><td><table style='border-collapse: collapse;'>");
        out
            .print("<tr><td class=\"tableValue\" colspan=\"5\"><font color=red size=3><b>FOR COMMAND CENTRE USE ONLY</b></font></td></tr>");
        out.print("<tr>");
        out.print("<td class=\"tableHeader\" colspan=\"5\">Mandatory Notification</td>");
        out.print("</tr>");
        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"350\">Contact</td>");
        out.print("<td class=\"tableHeader\" width=\"170\">Means of Notification</td>");
        out.print("<td class=\"tableHeader\" width=\"170\">Notification Details</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Updated by</td>");
        out.print("</tr>");

        while (rs.next())
        {
            String mandatoryContactName = HTMLTool.cleanString(rs.getString("contact_det"));
            String notificationMean = HTMLTool.cleanString(rs.getString("notif_mean"));
            String notificationDetail = HTMLTool.cleanString(rs.getString("notif_det"));
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
            String modifiedDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
                modifiedDate = dateFormat.format(rs.getDate("DT_UPD"));
            }
            out.print("<tr><td class=\"tableValue\">" + mandatoryContactName + "</td><td class=\"tableValue\">"
                      + (notificationMean.equalsIgnoreCase("p") ? "Phone" : "Email") 
                      + "</td><td class=\"tableValue\">" + notificationDetail + "</td>" +
                      "<td class=\"tableValue\">" + modifiedDate + "</td>" +
                      "<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }

        out.print("</table><br></td></tr>");
        rs.close();

        // added by Donatus
        String strURL = "http://ncs-iportal.optus.com.au/kbs/jsp/downloadexcel.jsp";
        out.print("<tr><td><table style='border-collapse: collapse;'>");
        out.print("<tr>");
        out.print("<td class=\"tableHeader\" colspan=\"1\">Auto Escalation</td>");
        out.print("</tr>");
        out.print("<tr><td class=\"tableValue\"><a href='" + strURL + "' target='_blank'>Click Here</a></td></tr>");
    }

    /**
     * Display Logging Calls
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 25/02/2011
     */
    protected void doLoggingCall(Connection con, int appid) throws SQLException, IOException
    {
        JspWriter out = pageContext.getOut();

        String sel_knownError = "SELECT * FROM IKBS_KNOWN_ERR WHERE APP_ID = '" + appid + "'";
        Statement s_knownError = con.createStatement();
        ResultSet rs_knownError = s_knownError.executeQuery(sel_knownError);
        if (rs_knownError.next())
        {
            String url_knownErorrs = "/ikbs/kbs/applicationDetails.do?appid=" + appid + "&type=technical";
            out.print("<div class=\"alphaMenu\"><a href='" + url_knownErorrs + "'>Known Errors</a></div><br>");
        }

        Statement s = con.createStatement();
        String sql = "select distinct TB_NAME  from IKBS_LOGGING_PROC where APP_ID = '" + appid+ "' ORDER BY TB_NAME";
        ResultSet tableNamers = s.executeQuery(sql);
        
        int i = 1;
        while (tableNamers.next())
        {
            if (tableNamers.getString("TB_NAME") != null && tableNamers.getString("TB_NAME") != "")
            {
                out.print("<div class=\"tableHeader\"><li><a href='#" + i++ + "'>" + tableNamers.getString("TB_NAME")
                          + "</a></li></div>");
            }
        }
        out.print("<br>");
        tableNamers.close();
        
        // Print table data with table name is null
        String sel = "SELECT l.*, g.NAME FROM IKBS_LOGGING_PROC l, IKBS_SKG g WHERE l.SKG_ID = g.ID AND l.APP_ID = '" + appid + "' AND TB_NAME IS NULL ORDER BY l.DISP_SEQ ";

        LOG.debug("Logging SQL" + sel);
        ResultSet rs = s.executeQuery(sel);
        boolean firstTime = true;
        
        while(rs.next()){
        	 if (firstTime){
        		 out.print("<tr>");
                 out.print("<td class=\"tableHeader\" width=\"250\">Problem Description</td>");
                 out.print("<td class=\"tableHeader\" align=\"center\" width=\"50\">Action</td>");
                 out.print("<td class=\"tableHeader\" width=\"100\">Skill Group</td>");
                 out.print("</tr>"); 
                 firstTime = false;
        	 }
        	 String problemDescription = HTMLTool.cleanString(rs.getString("prob_desc"));
             String groupName = HTMLTool.cleanString(rs.getString("name"));
             String loggingID = HTMLTool.cleanString(rs.getString("id"));
             String actionP = HTMLTool.cleanString(rs.getString("is_phone"));
             String actionD = HTMLTool.cleanString(rs.getString("is_dispatch"));
             String actionE = HTMLTool.cleanString(rs.getString("is_email"));
             String actionM = HTMLTool.cleanString(rs.getString("is_msg"));
             String actionF = HTMLTool.cleanString(rs.getString("is_fcr"));
             String action = formatLogAction(actionP, actionD, actionE, actionM, actionF);
             
             out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/loggingCallDetails.do?loggingID="
                     + loggingID + "' target=\"_popup\" " + " onClick =\"wopen('"
                     + "/ikbs/kbs/loggingCallDetails.do?loggingID=" + loggingID
                     + "', 'popup', 640, 400); return false;\" >" + problemDescription + "</a> "
                     + "</td><td align=\"center\" class=\"tableValue\">" + action + "</td>"
                     + "<td class=\"tableValue\">" + groupName + "</td></tr>");
        }
        rs.close();
        
        //print table name and table data
        sel = "SELECT l.*, g.NAME FROM IKBS_LOGGING_PROC l, IKBS_SKG g WHERE l.SKG_ID = g.ID AND l.APP_ID = '" + appid + "' AND l.TB_NAME IS NOT NULL ORDER BY l.TB_NAME, l.DISP_SEQ ";
        rs = s.executeQuery(sel);
        
        //result set for comparing the table name
        Statement s1 = con.createStatement();
        ResultSet rs1 = s1.executeQuery(sel);
        
        String nextTableName = "";
        int indexRowId = 1;
        boolean compareFlag = true;
        while(rs.next()){
        	String rsTableName = rs.getString("TB_NAME");       
        	if(rsTableName != null){
        		if (compareFlag == false) {
                    rs1.next();
                    nextTableName = rs1.getString("TB_NAME");                
                }
        		if (! nextTableName.equals(rsTableName)) 
                { 
        			  out.print("<tr><td colspan='3'>&nbsp;</td></tr>");
                      out.print("<tr><td><a name='" + indexRowId + "' id='" + indexRowId
                                + "'></a><div class=\"tableHeader\">" + rsTableName + "</div></td></tr>");  
                      out.print("<tr>");
                      out.print("<td class=\"tableHeader\" width=\"250\">Problem Description</td>");
                      out.print("<td class=\"tableHeader\" align=\"center\" width=\"50\">Action</td>");
                      out.print("<td class=\"tableHeader\" width=\"100\">Skill Group</td>");
                      out.print("</tr>"); 
                }
                      String problemDescription = HTMLTool.cleanString(rs.getString("prob_desc"));
                      String groupName = HTMLTool.cleanString(rs.getString("name"));
                      String loggingID = HTMLTool.cleanString(rs.getString("id"));
                      String actionP = HTMLTool.cleanString(rs.getString("is_phone"));
                      String actionD = HTMLTool.cleanString(rs.getString("is_dispatch"));
                      String actionE = HTMLTool.cleanString(rs.getString("is_email"));
                      String actionM = HTMLTool.cleanString(rs.getString("is_msg"));
                      String actionF = HTMLTool.cleanString(rs.getString("is_fcr"));
                      String action = formatLogAction(actionP, actionD, actionE, actionM, actionF);
                      
                      out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/loggingCallDetails.do?loggingID="
                              + loggingID + "' target=\"_popup\" " + " onClick =\"wopen('"
                              + "/ikbs/kbs/loggingCallDetails.do?loggingID=" + loggingID
                              + "', 'popup', 640, 400); return false;\" >" + problemDescription + "</a> "
                              + "</td><td align=\"center\" class=\"tableValue\">" + action + "</td>"
                              + "<td class=\"tableValue\">" + groupName + "</td></tr>");
                      indexRowId++;
                      compareFlag = false;
        	}
        }
        
        rs.close();
        rs1.close();
        
        
        ResultSet rs_export = s.executeQuery(sel);
        // Export to ... // TODO is it still used?
        if ((role.indexOf("KBS_EDIT_ROLE") >= 0))
        {

            out.print("<tr><td><table id=\"myTable\" style=\"display: none\" >");
            out.print("<tr>");
            out.print("<td class=\"tableHeader\" width=\"250\">Problem Description</td>");
            out.print("<td class=\"tableHeader\" width=\"50\">Skill Group</td>");
            out.print("<td class=\"tableHeader\" width=\"60\">Mandatory Question</td>");
            out.print("<td class=\"tableHeader\" width=\"80\">Product</td>");
            out.print("<td class=\"tableHeader\" width=\"80\">Problem Type</td>");
            out.print("<td class=\"tableHeader\" width=\"60\">Additional Info</td>");
            out.print("</tr>");
            while (rs_export.next())
            {
                String problemDescription = HTMLTool.cleanString(rs.getString("prob_desc"));
                String groupName = HTMLTool.cleanString(rs.getString("name"));
                String mandatoryQ = HTMLTool.cleanString(rs_export.getString("mandatory_qn"));
                String product = HTMLTool.cleanString(rs_export.getString("product"));
                String problemType = HTMLTool.cleanString(rs_export.getString("problem_type"));
                String addInfo = HTMLTool.cleanString(rs_export.getString("addl_info"));

                out.print("<tr><td  class=\"tableValue\"> " + problemDescription + " </td><td class=\"tableValue\">"
                          + groupName + "</td><td class=\"tableValue\">" + mandatoryQ
                          + "</td><td class=\"tableValue\">" + product + "</td><td class=\"tableValue\">" + problemType
                          + "</td><td class=\"tableValue\">" + addInfo + "</td></tr>");
            }
            out.print("</table></td><td>");

            out.print("</td></tr>");
        }

        rs_export.close();
        s.close();
    }

    /**
     * Display Logging Calls that matched the search criteria
     * 
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 25/02/2011
     */
    protected void doMatchedLog(Connection con, int appid) throws SQLException, IOException
    {
        JspWriter out = pageContext.getOut();

        String sel_knownError = "SELECT * FROM IKBS_KNOWN_ERR WHERE APP_ID = '" + appid + "'";
        Statement s_knownError = con.createStatement();
        ResultSet rs_knownError = s_knownError.executeQuery(sel_knownError);
        if (rs_knownError.next())
        {
            String url_knownErorrs = "/ikbs/kbs/applicationDetails.do?appid=" + appid + "&type=technical";
            out.print("<div class=\"alphaMenu\"><a href='" + url_knownErorrs + "'>Known Errors</a></div><br>");
        }

        ServletRequest request = pageContext.getRequest();
        String searchVal = request.getParameter("searchVal");

        Statement s = con.createStatement();
        String keyword[] = new String[3];
        keyword[0] = "%";
        keyword[1] = "%";
        keyword[2] = "%";
        String tmp[] = searchVal.split("\\s+");
        if (tmp.length == 1)
            keyword[0] = tmp[0].trim().toUpperCase();
        if (tmp.length == 2)
        {
            keyword[0] = tmp[0].trim().toUpperCase();
            keyword[1] = tmp[1].trim().toUpperCase();
        }
        if (tmp.length >= 3)
        {
            keyword[0] = tmp[0].trim().toUpperCase();
            keyword[1] = tmp[1].trim().toUpperCase();
            keyword[2] = tmp[2].trim().toUpperCase();
        }

        String sel = "SELECT DISTINCT l.ID FROM IKBS_APP a, IKBS_LOGGING_PROC l "
            + "where a.ID = l.APP_ID AND a.id = " + appid + " AND " +
            " ((l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) LIKE '%"
            + keyword[0] + "%" + keyword[1] + "%" + keyword[2] + "%'"
            + " OR "
            + "(l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) "
            + "LIKE "
            + "'%"
            + keyword[0] + "%" + keyword[2] + "%" + keyword[1]
            + "%'"
            + " OR "
            + "(l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) "
            + "LIKE "
            + "'%"
            + keyword[1] + "%" + keyword[0] + "%" + keyword[2]
            + "%'"
            + " OR "
            + "(l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) "
            + "LIKE "
            + "'%"
            + keyword[1] + "%" + keyword[2] + "%" + keyword[0]
            + "%'"
            + " OR "
            + "(l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) "
            + "LIKE "
            + "'%"
            + keyword[2] + "%" + keyword[0] + "%" + keyword[1]
            + "%'"
            + " OR "
            + "(l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) "
            + "LIKE "
            + "'%"
            + keyword[2] + "%" + keyword[1] + "%" + keyword[0] 
            + "%')";
        
        LOG.debug("MatchedLog SQL: " + sel);
        ResultSet rs = s.executeQuery(sel);
        String logIdList = "0";
        while (rs.next())
        {
            logIdList = logIdList + "," + rs.getString("id");

        }

        String sel1 = "SELECT l.*, g.NAME as groupName FROM IKBS_LOGGING_PROC l, IKBS_SKG g WHERE " +
        "l.SKG_ID = g.ID AND " +
        "l.ID in (" + logIdList + ") ORDER BY l.DISP_SEQ ";

        LOG.debug("MatchedLogID SQL: " + sel1);
        ResultSet rs1 = s.executeQuery(sel1);

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"250\">Problem Description</td>");
        out.print("<td class=\"tableHeader\" width=\"50\" align=\"center\">Action</td>");
        out.print("<td class=\"tableHeader\" width=\"100\">Skill Group</td>");
        out.print("</tr>");

        while (rs1.next())
        {

            String problemDescription = HTMLTool.cleanString(rs.getString("prob_desc"));
            String groupName = HTMLTool.cleanString(rs.getString("groupName"));
            String loggingID = HTMLTool.cleanString(rs.getString("id"));
            String actionP = HTMLTool.cleanString(rs.getString("is_phone"));
            String actionD = HTMLTool.cleanString(rs.getString("is_dispatch"));
            String actionE = HTMLTool.cleanString(rs.getString("is_email"));
            String actionM = HTMLTool.cleanString(rs.getString("is_msg"));
            String actionF = HTMLTool.cleanString(rs.getString("is_fcr"));
            String action = formatLogAction(actionP, actionD, actionE, actionM, actionF);
            
            out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/loggingCallDetails.do?loggingID="
                      + loggingID + "' target=\"_popup\" " + " onClick =\"wopen('"
                      + "/ikbs/kbs/loggingCallDetails.do?loggingID=" + loggingID
                      + "', 'popup', 640, 400); return false;\" >" + problemDescription + "</a> "
                      + "</td><td align=\"center\" class=\"tableValue\">" + action + "</td>"
                      + "<td class=\"tableValue\">" + groupName + "</td></tr>");

        }

        rs.close();
        s.close();
    }

    protected void doReference(Connection con, int appid) throws SQLException, IOException
    {

        String sel = "SELECT * FROM IKBS_REF WHERE APP_ID = '" + appid + "'";
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"590\">Description</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Updated by</td>");
        out.print("</tr>");

        while (rs.next())
        {
            Clob referenceDescription = rs.getClob("reference");            
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
            String modifiedDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
                modifiedDate = dateFormat.format(rs.getDate("DT_UPD"));
            }
            out.print("<tr><td class=\"tableValue\"><pre style='font-family: Arial'>"); 
              // Read CLOB data type
              if (referenceDescription != null) {
                  Reader clobData = referenceDescription.getCharacterStream();
                  int i = 0;
                  while ((i = clobData.read()) > 0)
                  {
                      out.print((char) i);
                  }
              }    
            out.print("</pre></td>" +
                      "<td class=\"tableValue\">" + modifiedDate + "</td>" +
                      "<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }

        rs.close();
        s.close();
    }
    
    /**
     * Display Known Error
     * @param con
     * @param appid
     * @throws SQLException
     * @throws IOException
     * @since 01/03/2011
     */
    protected void doKnownError(Connection con, int appid) throws SQLException, IOException
    {

        JspWriter out = pageContext.getOut();
        Statement s = con.createStatement();

        String sql = "select DISTINCT TB_NAME  from IKBS_KNOWN_ERR where APP_ID = " + appid + " AND TB_NAME IS NOT NULL ";
        ResultSet textIndexCon = s.executeQuery(sql);
        int i = 1;
        while (textIndexCon.next())
        {
            if (textIndexCon.getString("TB_NAME") != null && textIndexCon.getString("TB_NAME") != "")
            {
                out.print("<div class=\"tableHeader\"><li><a href='#" + i++ + "'>" + textIndexCon.getString("TB_NAME")
                          + "</a></li></div>");

            }
        }
        out.print("<br>");
        textIndexCon.close();

        // Print table data with table name is null       
        String sel = "SELECT ID, ERR_DESC, DT_UPD, UPD_BY FROM IKBS_KNOWN_ERR WHERE APP_ID = '" + appid + "' AND TB_NAME IS NULL order by err_desc ";
        LOG.debug("eror Description for tableName is null -- "+ sel);
        ResultSet rs = s.executeQuery(sel);
        boolean flag = true;
        while (rs.next())
        {
        	if (flag){
        		out.print("<tr>");
    	        out.print("<td class=\"tableHeader\" width=\"250\">Error Description</td>");
    	        out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
    	        out.print("<td class=\"tableHeader\" width=\"80\">Updated By</td>");
    	        out.print("</tr>");
    	        flag = false;
        	}
	    	             
            String knownErrorID = HTMLTool.cleanString(rs.getString("ID"));
            String errorDescription = HTMLTool.cleanString(rs.getString("ERR_DESC"));
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
            String modifiedDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
                modifiedDate = dateFormat.format(rs.getDate("DT_UPD"));
            }

            out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/knownErrorDetails.do?knownErrorID="
                      + knownErrorID + "' target=\"_popup\" " + " onClick =\"wopen('"
                      + "/ikbs/kbs/knownErrorDetails.do?knownErrorID=" + knownErrorID
                      + "', 'popup', 640, 400); return false;\" >" + errorDescription
                      + "</a></td><td class=\"tableValue\">" + modifiedDate + "</td>" +
                            "<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }

        rs.close();
        
        
        // Print table name and table data
        sel = "SELECT * FROM IKBS_KNOWN_ERR WHERE APP_ID = '" + appid + "' and TB_NAME is not null ORDER BY TB_NAME, DISP_SEQ ";
        rs = s.executeQuery(sel);
        
        // result set for comparing the table name
        Statement s1 = con.createStatement();
        ResultSet rs1 = s1.executeQuery(sel);

        String nextTableName = "";
        boolean firstime = true;
        int indexRowId = 1;
        while (rs.next())
        {
            String tableName = rs.getString("TB_NAME");            
            
            if ((tableName != null))
            {
                if (firstime == false) {
                    rs1.next();
                    nextTableName = rs1.getString("TB_NAME");                
                }
                System.out.println("current nextTableName-- " +nextTableName.toString());
                if (! nextTableName.equals(tableName)) 
                {            
                    out.print("<tr><td colspan='3'>&nbsp;</td></tr>");
                    out.print("<tr><td><a name='" + indexRowId + "' id='" + indexRowId
                              + "'></a><div class=\"tableHeader\">" + tableName + "</div></td></tr>");  
                    out.print("<tr>");
                    out.print("<td class=\"tableHeader\" width=\"250\">Error Description</td>");
                    out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
                    out.print("<td class=\"tableHeader\" width=\"80\">Updated By</td>");
                    out.print("</tr>");
                }
                
                String knownErrorID = HTMLTool.cleanString(rs.getString("id"));
                String errorDescription = HTMLTool.cleanString(rs.getString("err_desc"));
                String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
                String modifiedDate = "";
                if (rs.getDate("DT_UPD") != null)
                {
                    modifiedDate = dateFormat.format(rs.getDate("DT_UPD"));
                }
    
                out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/knownErrorDetails.do?knownErrorID="
                          + knownErrorID + "' target=\"_popup\" " + " onClick =\"wopen('"
                          + "/ikbs/kbs/knownErrorDetails.do?knownErrorID=" + knownErrorID
                          + "', 'popup', 640, 400); return false;\" >" + errorDescription
                          + "</a></td><td class=\"tableValue\">" + modifiedDate + "</td>" +
                          		"<td class=\"tableValue\">" + updatedBy + "</td></tr>");
                
                indexRowId++;
                firstime = false;
            }
        }

        rs.close();
        rs1.close();

        ResultSet rs_export = s.executeQuery(sel);
        // Export to Excel file
        if ((role.indexOf("KBS_EDIT_ROLE") >= 0))
        {
            out.print("<tr><td colspan='3'>&nbsp;</td></tr>");
            out
                .print("<tr><td><form><input type= \"button\" onclick=\"CreateExcelSheet()\" value= \"create excel file\"></form></td></tr>");

            out.print("<tr><td><table id=\"myTable\" style=\"display: none\" >");

            out.print("<tr>");
            out.print("<td class=\"tableHeader\" width=\"250\">Error Description</td>");
            out.print("<td class=\"tableHeader\" width=\"30\">Solution</td>");

            out.print("</tr>");
            while (rs_export.next())
            {
                String errorDescription = HTMLTool.cleanString(rs_export.getString("err_desc"));

                out.print("<tr><td  class=\"tableValue\"> " + errorDescription + " </td><td class=\"tableValue\">");
                
                Clob solution = rs_export.getClob("solution");
                
                // Read CLOB data type
                if (solution != null) {
                    Reader clobData = solution.getCharacterStream();
                    int chr = 0;
                    while ((chr = clobData.read()) > 0)
                    {
                        out.print((char) chr);
                    }
                }                
                
                out.print("</td></tr>");
            }
            out.print("</table></td><td>");
            out.print("</td></tr>");
        }

        rs_export.close();

        s.close();
    }


    protected void doCommandCenterKEDB(Connection con, int appid) throws SQLException, IOException
    {

        String sel = "select f.ID, f.symptom, s.name as groupName, f.dt_upd, f.upd_by " +
        		"from ikbs_cc_fix f, ikbs_cc_fix_skg fs, ikbs_skg s " +
        		"where f.id = fs.cc_fix_id " +
        		"and fs.skg_id = s.id " +
        		"and f.app_id= '" + appid + "'";
        
        LOG.debug("SQL CC KEDB: " + sel);
        
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"590\">Symptoms</td>");
        out.print("<td class=\"tableHeader\" width=\"100\">Skill Group</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Updated by</td>");        
        out.print("</tr>");

        while (rs.next())
        {
            String symptom = HTMLTool.cleanString(rs.getString("symptom"));
            String skilledGroup = HTMLTool.cleanString(rs.getString("groupName"));
            String ccNoteId = HTMLTool.cleanString(rs.getString("ID"));
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
            String modifiedDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
                modifiedDate =dateFormat.format(rs.getDate("DT_UPD"));
            }            
            out.print("<tr><td  class=\"tableValue\">  " + "<a href='/ikbs/kbs/commandCentreDetails.do?ccNoteId="
                      + ccNoteId + "' target=\"_popup\" " + " onClick =\"wopen('"
                      + "/ikbs/kbs/commandCentreDetails.do?ccNoteId=" + ccNoteId
                      + "', 'popup', 640, 400); return false;\" >" + symptom + "</a></td><td class=\"tableValue\">"
                      + skilledGroup + "</td>");
            out.print("<td class=\"tableValue\">" + modifiedDate + "</td>");
            out.print("<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }
    }

    protected void doCommandCenter(Connection con, int appid) throws SQLException, IOException
    {

        String sel = "SELECT * FROM IKBS_CC_NOTE WHERE APP_ID = '" + appid + "'";
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();

        out.print("<tr>");
        out.print("<td class=\"tableHeader\" width=\"590\">Description</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Last Modified</td>");
        out.print("<td class=\"tableHeader\" width=\"80\">Updated by</td>");
        out.print("</tr>");

        while (rs.next())
        {
            Clob commandDescription = rs.getClob("description");
            String updatedBy = HTMLTool.cleanString(rs.getString("UPD_BY"));
            String commandDate = "";
            if (rs.getDate("DT_UPD") != null)
            {
                commandDate = dateFormat.format(rs.getDate("DT_UPD"));
            }
            out.print("<tr><td class=\"tableValue\">"); 
            // Read CLOB data type
            if (commandDescription != null) {
                Reader clobData = commandDescription.getCharacterStream();
                int i = 0;
                while ((i = clobData.read()) > 0)
                {
                    out.print((char) i);
                }
            }            
            out.print("</td>" +
            		"<td class=\"tableValue\">" + commandDate + "</td>");
            out.print("<td class=\"tableValue\">" + updatedBy + "</td></tr>");
        }
    }

    // Deprecated - not used
    protected void doImage(Connection con, int appid) throws IOException
    {

        JspWriter out = pageContext.getOut();

        String imgUrl = "/ikbs/kbs/appImage.do?applicationId=" + appid;
        out.println("</table>");
        out.println("<body onload=popUp('" + imgUrl + "')>");

    }
    
    /**
     * To format the actions into their real values
     * For example:
     * if Phone is true, print "P"
     * if Email is true, print "E"
     * 
     * @param actionP
     * @param actionD
     * @param actionE
     * @param actionM
     * @param actionF
     * @return
     * @since 01/03/2011
     */
    private String formatLogAction(String actionP, String actionD, String actionE, String actionM, String actionF) 
    {
        StringBuffer action = new StringBuffer();
        if (actionP.equals("Y"))
        {
            action.append("P");
        }
        if (actionD.equals("Y"))
        {
            action.append("/D");
        }
        if (actionE.equals("Y"))
        {
            action.append("/E");
        }
        if (actionM.equals("Y"))
        {
            action.append("/M");
        }
        if (actionF.equals("Y"))
        {
            if (action.length() > 0) 
            {
                action.append("/");                    
            }                   
            action.append("F");
        }  
        
        return action.toString();
    }

    protected static PrintWriter pw;

    public static void openLogFile(String sLogFile)
    {
        try
        {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(sLogFile, true)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void writeToLogFile(String sText)
    {
        try
        {
            pw.print(printTimeNow("yyyy.MM.dd G 'at' hh:mm:ss z") + " ===> ");
            pw.println(sText);
            pw.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void closeLogFile()
    {
        try
        {
            pw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String printTimeNow(String format)
    {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String datenewformat = formatter.format(today);
        return datenewformat;
    }
}
