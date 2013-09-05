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

public class SolutionDetails extends TagSupport {

    public int doStartTag()
            throws JspException {

        Connection con = null;

        try {
            JspWriter out = pageContext.getOut();
            ServletRequest request = pageContext.getRequest();

            String type = request.getParameter("type");

            String solid = request.getParameter("solid");
            
            if(solid != null && !solid.equals("")) {
                
                int solidInt = 0;
                
                try {
                    solidInt = Integer.parseInt(solid);
                } catch(NumberFormatException e) {
                    throw new JspException("Solution ID not a number.");
                }   

                Transaction txn = new Transaction();
                con = txn.getConnection();
                Statement s = con.createStatement();

                // Get solution
                String subject = "";
                String domain = "";
                String solclass = "";

                String sel1 = "SELECT SUBJECT, DOMAIN, SOLCLASS FROM KBS_SOLUTION WHERE SOLID = '" + solidInt + "'";
                ResultSet rs = s.executeQuery(sel1);

                if(rs.next()) {
                    subject = HTMLTool.cleanString(rs.getString("SUBJECT"));
                    domain = HTMLTool.cleanString(rs.getString("DOMAIN"));
                    solclass = HTMLTool.cleanString(rs.getString("SOLCLASS"));
                }

                rs.close();

                // Get other tables
                String goal = selectDescription(s, "SELECT DESCRIPTION FROM KBS_GOAL WHERE SOLID = '" + solidInt + "'");
                String cause = selectDescription(s, "SELECT DESCRIPTION FROM KBS_CAUSE WHERE SOLID = '" + solidInt + "'");
                String symptom = selectDescription(s, "SELECT DESCRIPTION FROM KBS_SYMPTOM WHERE SOLID = '" + solidInt + "'");
                String resolution = selectDescription(s, "SELECT DESCRIPTION FROM KBS_RESOLUTION WHERE SOLID = '" + solidInt + "'");
                String keyword = selectDescription(s, "SELECT DESCRIPTION FROM KBS_KEYWORD WHERE SOLID = '" + solidInt + "'");

                // Output
                out.print("<tr><td class=\"tableHeader\" width=\"100\">Subject</td><td class=\"tableValue\" width=\"350\">" + subject + "</td></tr>");
                out.print("<tr><td class=\"tableHeader\" width=\"100\">ID</td><td class=\"tableValue\" width=\"350\">" + solidInt + "</td></tr>");
                out.print("<tr><td class=\"tableHeader\" width=\"100\">Domain</td><td class=\"tableValue\" width=\"350\">" + domain + "</td></tr>");
                out.print("<tr><td class=\"tableHeader\" width=\"100\">Solution Class</td><td class=\"tableValue\" width=\"350\">" + solclass + "</td></tr>");
                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">&nbsp;</td></tr>");

                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Goal(s)</td></tr>");
                out.print(goal);

                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Cause(s)</td></tr>");
                out.print(cause);

                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Symptom(s)</td></tr>");
                out.print(symptom);

                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Resolution(s)</td></tr>");
                out.print(resolution);

                out.print("<tr><td class=\"tableHeader\" colspan=\"2\">Keyword(s)</td></tr>");
                out.print(keyword);

                s.close();
                con.close();
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

    private String selectDescription(Statement s, String sel)
            throws SQLException {

        String res = "<tr><td class=\"tableValue\" colspan=\"2\">&nbsp;</td></tr>";
        ResultSet rs = s.executeQuery(sel);

        int i = 1;

        if(rs.next()) {
            res = "<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(rs.getString(1)) + "</td></tr>";
            i++;

            while(rs.next()) {
                res = res + "<tr><td class=\"tableValue\" colspan=\"2\">" + i + ". " + HTMLTool.cleanString(rs.getString(1)) + "</td></tr>";
                i++;
            }
        }

        rs.close();

        return res;
    }

}