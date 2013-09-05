package com.ncs.ikbs.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.ncs.ikbs.util.HTMLTool;
import com.ncs.ikbs.util.Transaction;


public class RRDetails extends TagSupport {

    public int doStartTag()
            throws JspException {

        Connection con = null;

        try {
            ServletRequest request = pageContext.getRequest();
            
            String type = request.getParameter("type");

            if(type != null && !type.equals("")) {
                String nodeid = request.getParameter("nodeid");

                if(nodeid == null || nodeid.equals("")) { 
                    throw new JspException("Require value for parameter nodeid");
                }
                
                int nodeidInt = 0;
                
                try {
                    nodeidInt = Integer.parseInt(nodeid);
                } catch(NumberFormatException e) {
                    throw new JspException("Node ID not a number");
                }

                Transaction txn = new Transaction();
                con = txn.getConnection();

                if(type.equals("info")) {
                    doInfo(con, nodeidInt);

                } else if(type.equals("roles")) {
                    doRoles(con, nodeidInt);

                } else if(type.equals("command")) {
                    doCommand(con, nodeidInt);
                    
                } else {
                    throw new JspException("Unknown value for parameter type");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException(e);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspException(e);

        } finally {
            if(con != null) {
                try {
                    if(!con.isClosed()) {
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println("FATAL: Could not close Connection!");
                    e.printStackTrace();
                }
            }
        }

        return SKIP_BODY;
    }

    private void doInfo(Connection con, int nodeid)
            throws SQLException, IOException {

        String sel = "SELECT NODENAME, LOB, PURPOSE, STATUS, DATEFORSTATUS, OPERATINGSYSTEM, IPADDRESS, NCLUSTER,  MODEL, ACCESSMETHOD, AUTOESCCODE, NDATABASE, SITE, PDTCOORDINATOR, PDTCONTACTNO, CSM, NOTES " +
        				"FROM KBS_NODE " +
        				"WHERE NODEID = '" + nodeid + "'";

        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        JspWriter out = pageContext.getOut();

        while(rs.next()) {
            String noden = HTMLTool.cleanString(rs.getString("NODENAME"));
            String liofb = HTMLTool.cleanString(rs.getString("LOB"));
            String purpo = HTMLTool.cleanString(rs.getString("PURPOSE"));
            String statu = HTMLTool.cleanString(rs.getString("STATUS"));
            String datef = HTMLTool.cleanString(rs.getString("DATEFORSTATUS"));
            String opers = HTMLTool.cleanString(rs.getString("OPERATINGSYSTEM"));
            String ipadd = HTMLTool.cleanString(rs.getString("IPADDRESS"));
            String clust = HTMLTool.cleanString(rs.getString("NCLUSTER"));
            String model = HTMLTool.cleanString(rs.getString("MODEL"));
            String accss = HTMLTool.cleanString(rs.getString("ACCESSMETHOD"));
            String autoe = HTMLTool.cleanString(rs.getString("AUTOESCCODE"));
            String datab = HTMLTool.cleanString(rs.getString("NDATABASE"));
            String site  = HTMLTool.cleanString(rs.getString("SITE"));
            String pcoor = HTMLTool.cleanString(rs.getString("PDTCOORDINATOR"));
            String pcont = HTMLTool.cleanString(rs.getString("PDTCONTACTNO"));
            String csm   = HTMLTool.cleanString(rs.getString("CSM"));
            String notes = HTMLTool.cleanString(rs.getString("NOTES"));
            
            out.print("<tr><td class=\"tableHeader\" colspan=\"2\"><center>Node Information</center></td></tr>");
            out.print("<tr><td class=\"tableHeader\">Node ID:</td><td class=\"tableValue\">" + nodeid + "</td>");
            out.print("<tr><td class=\"tableHeader\">Node Name:</td><td class=\"tableValue\">" + noden + "</td>");
            out.print("<tr><td class=\"tableHeader\">LOB:</td><td class=\"tableValue\">" + liofb + "</td>");
            out.print("<tr><td class=\"tableHeader\">Purpose:</td><td class=\"tableValue\">" + purpo + "</td>");
            out.print("<tr><td class=\"tableHeader\">Status:</td><td class=\"tableValue\">" + statu + "</td>");
            out.print("<tr><td class=\"tableHeader\">Date For Status:</td><td class=\"tableValue\">" + datef + "</td>");
            out.print("<tr><td class=\"tableHeader\">Operating System:</td><td class=\"tableValue\">" + opers + "</td>");
            out.print("<tr><td class=\"tableHeader\">IP Address:</td><td class=\"tableValue\">" + ipadd + "</td>");
            out.print("<tr><td class=\"tableHeader\">Cluster:</td><td class=\"tableValue\">" + clust + "</td>");
            out.print("<tr><td class=\"tableHeader\">Model:</td><td class=\"tableValue\">" + model + "</td>");
            out.print("<tr><td class=\"tableHeader\">Access Method:</td><td class=\"tableValue\">" + accss + "</td>");
            out.print("<tr><td class=\"tableHeader\">Auto Escalation Code:</td><td class=\"tableValue\">" + autoe + "</td>");
            out.print("<tr><td class=\"tableHeader\">Database:</td><td class=\"tableValue\">" + datab + "</td>");
            out.print("<tr><td class=\"tableHeader\">Site:</td><td class=\"tableValue\">" + site + "</td>");
            out.print("<tr><td class=\"tableHeader\">Production Coordinator:</td><td class=\"tableValue\">" + pcoor + "</td>");
            out.print("<tr><td class=\"tableHeader\">Production Coordinator Contact Number:</td><td class=\"tableValue\">" + pcont + "</td>");
            out.print("<tr><td class=\"tableHeader\">CSM:</td><td class=\"tableValue\">" + csm + "</td>");
            out.print("<tr><td class=\"tableHeader\">Notes:</td><td class=\"tableValue\">" + notes + "</td>");
        }

        rs.close();
        s.close();
    }
        
    private void doRoles(Connection con, int nodeid)
            throws SQLException, IOException {

        JspWriter out = pageContext.getOut();
        
        // Get each group
        String cao = getNames(con, "SELECT NAME FROM KBS_NODE_CUSTAPPOWNER WHERE NODEID = '" + nodeid + "' ORDER BY NAME");
        String car = getNames(con, "SELECT NAME FROM KBS_NODE_CUSTAPPREP WHERE NODEID = '" + nodeid + "' ORDER BY NAME");
        String sys = getNames(con, "SELECT NAME FROM KBS_NODE_SYSADMIN WHERE NODEID = '" + nodeid + "' ORDER BY NAME");
        String sec = getNames(con, "SELECT NAME FROM KBS_NODE_SECADMIN WHERE NODEID = '" + nodeid + "' ORDER BY NAME");
        
        // Print each group
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\"><center>Roles and Responsibilities</center></td></tr>");
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Customer Application Owners</td></tr>");
        out.print(cao);
        
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Customer Application Representatives</td></tr>");
        out.print(car);
        
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\">System Administrators</td></tr>");
        out.print(sys);
        
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Security Administrators</td></tr>");
        out.print(sec);
        
        // Do OCC
        out.print("<tr><td class=\"tableHeader\" colspan=\"2\">OCC Change Requests</td></tr>");
        
        String sel = "SELECT NAME, AUTH FROM KBS_NODE_OCC WHERE NODEID = '" + nodeid + "' ORDER BY NAME";
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        int i = 1;
        String name = "";
        int auth = 0;
        String authStr = "";
        while(rs.next()) {
            name = rs.getString(1);
            auth = rs.getInt(2);
            authStr = (auth == 1) ? "yes" : "no";
            out.print("<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(name) + "(Outage Auth: " + authStr + ")</td></tr>");
            i++;

            //while(rs.next()) {
            //    out.print("<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(name) + "(Outage Auth: " + authStr + ")</td></tr>");
            //    i++;
            //}
        }

        rs.close();
        s.close();
    }

    private void doCommand(Connection con, int nodeid)
    throws SQLException, IOException {

    	String sel = "SELECT DESCRIPTION, MODIFIEDDATE " +
				"FROM KBS_NODE_COMMANDNOTE " +
				"WHERE NODEID = '" + nodeid + "' ORDER BY COMMANDNOTEID";

		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery(sel);
		JspWriter out = pageContext.getOut();

	    out.print("<tr><td width=\"500\" class=\"tableHeader\">Description</td><td class=\"tableHeader\">Last Modified</td>");

		while(rs.next()) {
		    String descr = HTMLTool.cleanString(rs.getString("DESCRIPTION"));
		    String moddate = HTMLTool.cleanString(rs.getString("MODIFIEDDATE"));
		    out.print("<tr><td class=\"tableValue\">" + descr +"</td><td class=\"tableValue\">" + moddate + "</td>");
		}
		
		rs.close();
		s.close();
}

    private String getNames(Connection con, String sel)
    		throws SQLException {
        
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sel);
        
        StringBuffer nTable = new StringBuffer("<tr><td class=\"tableValue\" colspan=\"2\">&nbsp;</td></tr>");
        int i = 1;
        
        if(rs.next()) {
            nTable = new StringBuffer("<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(rs.getString(1)) + "</td></tr>");
            i++;

            while(rs.next()) {
                nTable.append("<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(rs.getString(1)) + "</td></tr>");
                i++;
            }
            
            nTable.append("<tr><td class=\"tableValue\" colspan=\"2\">&nbsp;</td></tr>");
        }
        
        rs.close();
        s.close();
        
        return nTable.toString();
    }
}
    
    
    