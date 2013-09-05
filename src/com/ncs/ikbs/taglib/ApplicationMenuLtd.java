package com.ncs.ikbs.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.ncs.ikbs.util.Transaction;

/** 
 * TODO Provide description 
 * 
 * @author NCS - updated by Cheng Zhang
 * @since 26/05/2011 
 */
public class ApplicationMenuLtd extends TagSupport {

    public int doStartTag()
            throws JspException {

        // Check request
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        String firstLetter = request.getParameter("firstLetter");
        
        System.out.println("ApplicationMenuLtd.java -- firstLetter == " + firstLetter);
        
        if(firstLetter == null) {
            if(request.getParameter("searchVal") != null) {
                firstLetter = "";
            } else {
                firstLetter = "A";
            }
        }
        
        HttpSession session = pageContext.getSession();
        
        ArrayList clist = (ArrayList)session.getAttribute("alphaListLtd");
        
        if(clist == null) {
            clist = getList();
            session.setAttribute("alphaListLtd", clist);
        }
        
        // Detect browser
        doIEMenu(clist, firstLetter);
            
        return SKIP_BODY;
    }
    
    public void doIEMenu(ArrayList clist, String firstLetter)
    		throws JspException {
        
        try {
            
            // Output menu
            JspWriter out = pageContext.getOut();
            out.println("<form method=\"POST\" action=\"/ikbs/kbs/applicationListLtd.do\">");
            out.println("<font class=\"pageText\">Or List Applications by First Letter: </font> ");
            out.print("<select name=\"firstLetter\" onchange=\"this.form.submit()\">");
            
            if(firstLetter.equals("")) {
                out.println("<option selected></option>");
            }
            
            for(int i = 0; i < clist.size(); i++) {
                String a = (String)clist.get(i);
                a = a.toUpperCase();
                                
                out.print("<option");
                
                if(a.equals(firstLetter) || clist.size() == 1) {
                    out.print(" selected");
                }
                
                out.println(">" + a + "</option>");
            }
            
            out.println("</select>");
            out.println("</form>");
            
        } catch(IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        
        }
    }
    
    private ArrayList getList()
    		throws JspException {
        
        ArrayList clist = null;
        Connection con = null;
        String loginName = (String) pageContext.getSession().getAttribute("loginName");
        
        try {
	        Transaction txn = new Transaction(true);
	        con = txn.getConnection();
	        
	        String sel = "SELECT DISTINCT(SUBSTR(a.S_NAME, 0, 1)) AS LETTER " +
	        			 "FROM IKBS_APP a, ikbs_user_app u " +
	        			 "WHERE u.app_id=a.id and u.user_id ='"+ loginName +"' ORDER BY LETTER";
	        Statement s = con.createStatement();
	        ResultSet rs = s.executeQuery(sel);
	        
	        clist = new ArrayList();
	        
	        while(rs.next()) {
	            clist.add(rs.getString(1));
	        }
	        
	        rs.close();
	        s.close();
	        con.close();
	        
        } catch(SQLException e) {
            e.printStackTrace();
            throw new JspException(e);
            
        } finally {
            if(con != null) {
                try {
                    if(!con.isClosed()) {
                        con.close();
                    }
                } catch(SQLException e) {
                    System.err.println("FATAL: Could not close Connection!");
                    e.printStackTrace();
                }
            }
        }	       

        return clist;
    }
}