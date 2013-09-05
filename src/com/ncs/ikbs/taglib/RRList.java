package com.ncs.ikbs.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.ncs.ikbs.util.Transaction;
import com.ncs.ikbs.vo.AppListVO;

public class RRList extends TagSupport {

    public int doStartTag()
            throws JspException {
        
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        // Get parameters nodeName, appName, lob
        String nodeName = request.getParameter("nodeName");
        String appName = request.getParameter("appName");
        String lob = request.getParameter("lob");
        
        // Fix parameters
        nodeName = fixParameter(nodeName);
        appName = fixParameter(appName);
        
        if(lob == null || lob.equals("All")) {
            lob = "%";
        } else {
            lob = lob.toUpperCase();
        }
        
        // Check for default search - limits results
        if(nodeName.equals("%") && appName.equals("%") && lob.equals("%")) {
            nodeName = "A%";
        }
        
        // BE AWARE: This search will NOT show Nodes that don't have applications
        // I'm assuming all nodes will have applications (so it appears in the
        // spreadsheet)... is this a bad assumption?
        String sel = 	"SELECT " +
        				"  A.NODEID, A.NODENAME, B.APPID, B.APPNAME " +
        				"FROM " +
        				"  KBS_NODE A, KBS_APPLICATION B, KBS_NODE2APPLICATION C " +
        				"WHERE " +
        				"  UPPER(A.NODENAME) LIKE '" + nodeName + "' AND " +
        				"  UPPER(B.APPNAME) LIKE '" + appName + "' AND " +
        				"  UPPER(A.LOB) LIKE '" + lob + "' AND " +        				
        				"  C.APPID = B.APPID AND C.NODEID = A.NODEID " + 
        				"ORDER BY A.NODENAME, B.APPNAME, A.NODEID";
        
        pageContext.getServletContext().log("*** R&R LIST STATEMENT:\n" + sel + "\n");
        
        
        doSearch(pageContext, sel);
        
        return SKIP_BODY;
    }
    
    public static void doSearch(PageContext pageContext, String sel)
    		throws JspException {
        
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        // Detect browser
        String userAgent = request.getHeader("User-Agent");
        
        if(userAgent!= null) {
            userAgent = userAgent.toLowerCase();
            
            if(userAgent.indexOf("msie") != -1) {
                doIESearch(pageContext, sel);
            } else {
                doNetscapeSearch(pageContext, sel);
            }   
        }
    }
        
    public static void doIESearch(PageContext pageContext, String sel)
    		throws JspException {
        
        Connection con = null;

        try {
            JspWriter out = pageContext.getOut();
            
            Transaction txn = new Transaction(true);
            con = txn.getConnection();
            
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sel);
            
            // Each Node has a list of Applications.
            // NB. I'm assuming NODEID's come out grouped
            // here. Don't change the "ORDER BY" for
            // the above SELECT statement!
            TreeMap nodeList = new TreeMap();
            AppListVO appList = null;
            int currId = 0;
            
            while(rs.next()) {
                int nodeId = rs.getInt(1);
                String nodeName = rs.getString(2);
                int appId = rs.getInt(3);
                String appName = rs.getString(4);
                
                if(nodeId != currId) {
                    appList = new AppListVO();
                    appList.setNodeId(nodeId);
                    appList.setNodeName(nodeName);
                    String nkey = nodeName + ":" + nodeId;
                    nodeList.put(nkey, appList);  // NB: NodeName:NodeID as key because I'm on a deadline!
                    nodeId = currId;
                }
                
                appList.addApplication(appId, appName);
            }
                    
            rs.close();
            s.close();
            
            if(nodeList.size() > 0) {

                // Begin tree
                out.println("<div class=\"dtree\">\n");
                out.println("<p><a href=\"javascript: d.openAll();\">open all</a> | <a href=\"javascript: d.closeAll();\">close all</a></p>");

                out.println("<script type=\"text/javascript\">");
                out.println("<!--");
                
                out.println("  d = new dTree('d');\n");
                out.println("  d.add(0, -1, 'Results');");
                
                int index = 0;
                
                for(Iterator i = nodeList.keySet().iterator(); i.hasNext();) {
                    String keyStr = (String)i.next();
                    String npair[] = keyStr.split(":");
                    String nodeName = npair[0];
                    String idStr = npair[1];
                    
                    // Dump NodeName - start branch
                    index++;
                    int nodeParent = index;
                    out.println("  d.add(" + nodeParent + ", 0, '" + nodeName + "');");
                    
                    // Dump Info leaf
                    index++;
                    String link = "/ikbs/kbs/rrDetails.do?nodeid=" + idStr + "&type=info";
                    out.println("  d.add(" + index + ", " + nodeParent + ", 'Info', '" + link + "', 'Info', 'detailsFrame');");
                    
                    // Dump Roles leaf
                    index++;
                    link = "/ikbs/kbs/rrDetails.do?nodeid=" + idStr + "&type=roles";
                    out.println("  d.add(" + index + ", " + nodeParent + ", 'Roles', '" + link + "', 'Roles', 'detailsFrame');");

                    // Dump Command Notes leaf
                    index++;
                    link = "/ikbs/kbs/rrDetails.do?nodeid=" + idStr + "&type=command";
                    out.println("  d.add(" + index + ", " + nodeParent + ", 'Command Centre', '" + link + "', 'Command', 'detailsFrame');");
                    
                    // Dump Applications branch
                    index++;
                    int appParent = index;
                    out.println("  d.add(" + index + ", " + nodeParent + ", 'Applications');");
                    
                    AppListVO vo = (AppListVO)nodeList.get(keyStr);
                    
                    if(vo != null) {
                        for(Iterator j = vo.applicationsIterator(); j.hasNext();) {
                            String appName = (String)j.next();
                            String appId = vo.getApplicationId(appName);
                            link = "/ikbs/kbs/applicationDetails.do?appid=" + appId + "&type=contact";
                            
                            index++;
                            out.println("  d.add(" + index + ", " + appParent + ", '" + appName + "', '" + link + "', '" + appName + "', 'detailsFrame');");
                        }
                    }
                    
                    out.println();
                }
                
                out.println("  document.write(d);");
                out.println("  d.closeAll();");
                out.println("  //-->");
                out.println("</script>");
                out.println("</div>\n");
                
                // Clear memory (hoping GC does it's job here)
                nodeList.clear();
            
            } else {
                out.println("<font class=\"pageText\">No results.</font>\n");
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
    }
    
    public static void doNetscapeSearch(PageContext pageContext, String sel)
    		throws JspException {
        
        // To Implement - Netscape version of output...
        // Man, I'd hate to do this one! :)
        try {
            JspWriter out = pageContext.getOut();
            out.write("Not Implemented.");
        } catch(IOException e) {
            throw new JspException(e);
        }
        
    }
    
    private String fixParameter(String p) {
        
        if(p == null || p.equals("")) {
            return "%";
        
        } else if(p.indexOf("%") == -1) {
            return p.toUpperCase() + "%";
        
        } else {
            return p.toUpperCase();
        }
    }
}