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

import com.ncs.ikbs.util.Transaction;


public class SolutionList extends TagSupport {
	
	public int doStartTag()
		throws JspException {
		
		//String sel = "SELECT * FROM KBS_SOLUTION ORDER BY SUBJECT";
		String sel = "SELECT * FROM KBS_SOLUTION_FOLDER ORDER BY ID";
		doNormalSearch(pageContext, sel);
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
	
	public static void doNormalSearch(PageContext pageContext, String sel)
		throws JspException {
		
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		
		// Detect browser
		String userAgent = request.getHeader("User-Agent");
		
		if(userAgent!= null) {
			userAgent = userAgent.toLowerCase();
			
			if(userAgent.indexOf("msie") != -1) {
				doNormalIESearch(pageContext, sel);
				
			} else {
				sel = "SELECT * FROM KBS_SOLUTION ORDER BY SUBJECT";
				doNormalNetscapeSearch(pageContext, sel);
			}
		}
	}
	
	public static void doNormalIESearch(PageContext pageContext, String sel)
		throws JspException {
		
		Connection con = null;
		
		try {
			JspWriter out = pageContext.getOut();
			ServletRequest request = pageContext.getRequest();
			
			Transaction txn = new Transaction(true);
			con = txn.getConnection();
			
			// Begin tree
			out.println("<div class=\"dtree\">\n");
			//out.println("<p><a href=\"javascript: d.openAll();\">open all</a> | <a href=\"javascript: d.closeAll();\">close all</a></p>");
			
			out.println("<script type=\"text/javascript\">");
			out.println("<!--");
			
			out.println("  d = new dTree('d');\n");
			out.println("  d.add(0, -1, 'Results');");
			
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sel);
			
			while(rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				int parent = rs.getInt("PARENT");
				name = name.replaceAll("'", "&#39;");
				
				out.println("  d.add(" + id + ", "+parent+", '" + name + "');");
			} 
			/*else {
				out.println("<font class=\"pageText\">No results.</font>\n");
			}*/
			ResultSet rs1 = s.executeQuery("SELECT * FROM KBS_SOLUTION ORDER BY SUBJECT");
			if (rs1.next())
			{
				int index = 5000;
				do {
					int solID = rs1.getInt("SOLID");
					String subj = rs1.getString("SUBJECT");
					subj = subj.replaceAll("'", "&#39;");
					int parent1 = rs1.getInt("PARENT");
					
					index++;
					out.println("  d.add('" + index + "', "+parent1+", '" + subj + "', '/ikbs/kbs/solutionDetails.do?solid=" + solID + "', '', 'detailsFrame');"); 
					
				} while(rs1.next());
			}
			
			out.println("  document.write(d);");
			out.println("  d.closeAll();");
			out.println("  //-->");
			out.println("</script>");
			out.println("</div>\n");
			
			rs.close();
			s.close();
			con.close();
			
		} catch(IOException e) {
			e.printStackTrace();
			throw new JspException(e);
			
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
	}
	
	public static void doNormalNetscapeSearch(PageContext pageContext, String sel)
		throws JspException {
		
		Connection con = null;
		
		try {
			JspWriter out = pageContext.getOut();
			
			Transaction txn = new Transaction(true);
			con = txn.getConnection();
			
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sel);
			
			if(rs.next()) {
				
				// Output radio boxes
				do {
					String solID = rs.getString("SOLID");
					String subj = rs.getString("SUBJECT");
					
					// Fix appName for tree
					subj = subj.replaceAll("'", "&#39;");
					
					out.println("<img src='/kbs/img/page.gif' valign='middle'> <a target='detailsFrame' href='/ikbs/kbs/solutionDetails.do?solid=" + solID + "'><font class='pageText'>" + subj + "</font></a><br>");
					
				} while(rs.next());
				
			} else {
				out.println("<font class=\"pageText\">No results.</font>\n");
			}
			
			rs.close();
			s.close();
			con.close();
			
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
	
	public static void doIESearch(PageContext pageContext, String sel)
    		throws JspException {
        
        Connection con = null;

        try {
            JspWriter out = pageContext.getOut();
            ServletRequest request = pageContext.getRequest();

            Transaction txn = new Transaction(true);
            con = txn.getConnection();
            
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sel);
            
            if(rs.next()) {
                
                // Begin tree
                out.println("<div class=\"dtree\">\n");
                //out.println("<p><a href=\"javascript: d.openAll();\">open all</a> | <a href=\"javascript: d.closeAll();\">close all</a></p>");

                out.println("<script type=\"text/javascript\">");
                out.println("<!--");
                
                out.println("  d = new dTree('d');\n");
                out.println("  d.add(0, -1, 'Results');");
                
                int index = 5000;
                
                do {
	                int solID = rs.getInt("SOLID");
	                String subj = rs.getString("SUBJECT");
	                subj = subj.replaceAll("'", "&#39;");
	                
	                index++;
	                out.println("  d.add('" + index + "', 0, '" + subj + "', '/ikbs/kbs/solutionDetails.do?solid=" + solID + "', '', 'detailsFrame');"); 
	                
                } while(rs.next());
                
                out.println("  document.write(d);");
                out.println("  d.closeAll();");
                out.println("  //-->");
                out.println("</script>");
                out.println("</div>\n");

            } else {
                out.println("<font class=\"pageText\">No results.</font>\n");
            }
            
            rs.close();
            s.close();
            con.close();

        } catch(IOException e) {
            e.printStackTrace();
            throw new JspException(e);

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
    }
    
    public static void doNetscapeSearch(PageContext pageContext, String sel)
			throws JspException {
		
		Connection con = null;
		
		try {
		    JspWriter out = pageContext.getOut();
		    
		    Transaction txn = new Transaction(true);
		    con = txn.getConnection();
		    
		    Statement s = con.createStatement();
		    ResultSet rs = s.executeQuery(sel);
		    
		    if(rs.next()) {
		        
		        // Output radio boxes
		        do {
		            String solID = rs.getString("SOLID");
		            String subj = rs.getString("SUBJECT");
		            
		            // Fix appName for tree
		            subj = subj.replaceAll("'", "&#39;");
		            
		            out.println("<img src='/kbs/img/page.gif' valign='middle'> <a target='detailsFrame' href='/ikbs/kbs/solutionDetails.do?solid=" + solID + "'><font class='pageText'>" + subj + "</font></a><br>");
		            
		        } while(rs.next());
		            		    
		    } else {
		        out.println("<font class=\"pageText\">No results.</font>\n");
		    }
		    
		    rs.close();
		    s.close();
		    con.close();
		
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
}