package com.ncs.ikbs.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.ncs.ikbs.util.Transaction;

public class LobSelect extends TagSupport {
    
    public int doStartTag()
            throws JspException {

        // Check request
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        String lobSelected = request.getParameter("lob");
        
        if(lobSelected == null) {
            lobSelected = "All";
        }
        
        HttpSession session = pageContext.getSession();
        
        ArrayList llist = (ArrayList)session.getAttribute("lobList");
        
        if(llist == null) {
            llist = this.getList();
            session.setAttribute("lobList", llist);
        }
        
        // Detect browser (not implemented)
        doIESelect(llist, lobSelected);
            
        return SKIP_BODY;
    }
    
    public void doIESelect(ArrayList llist, String lobSelected)
    		throws JspException {
        
        try {
            
            // Output menu
            JspWriter out = pageContext.getOut();
            out.println("<select name=\"lob\">");
            
            for(Iterator i = llist.iterator(); i.hasNext();) {
                String n = (String)i.next();
                String selected = "";
                
                if(n != null) {
                    if(n.equals(lobSelected)) {
                        selected = " selected";
                    }
                    
                    out.println("<option" + selected + ">" + n + "</option>");
                }
            }

            out.println("</select>");
            
        } catch(IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        
        }
    }
    
    private ArrayList getList()
    		throws JspException {
        
        ArrayList llist = null;
        Connection con = null;
        
        try {
	        Transaction txn = new Transaction(true);
	        con = txn.getConnection();
	        
	        String sel = "SELECT DISTINCT(LOB) AS DLOB FROM KBS_NODE ORDER BY DLOB";
	        Statement s = con.createStatement();
	        ResultSet rs = s.executeQuery(sel);
	        
	        ServletContext ctx = pageContext.getServletContext();
	        ctx.log(sel);
	        
	        llist = new ArrayList();
	        llist.add("All");
	        
	        while(rs.next()) {
	            llist.add(rs.getString(1));
	        }
	        
	        rs.close();
	        s.close();
	        
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

        return llist;
    }
}