package com.ncs.ikbs.taglib;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SolutionSearch extends TagSupport {

    public int doStartTag()
            throws JspException {
        
        ServletRequest request = pageContext.getRequest();
        String searchVal = request.getParameter("searchVal");
        
        if(searchVal != null && !searchVal.equals("")) {
            String sel = "SELECT * FROM KBS_SOLUTION WHERE SOLID IN (" +
            		"  SELECT SOLID FROM KBS_KEYWORD WHERE UPPER(DESCRIPTION) LIKE '%" + searchVal.toUpperCase() + "%'" +
            		") ORDER BY SUBJECT";
            
            System.err.println(sel);
            
            SolutionList.doSearch(pageContext, sel);
            
        } else {
            String sel = "SELECT * FROM KBS_SOLUTION ORDER BY SUBJECT";
            SolutionList.doSearch(pageContext, sel);
        }

        
        return SKIP_BODY;
    }       
}