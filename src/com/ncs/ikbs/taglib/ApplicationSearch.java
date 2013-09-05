package com.ncs.ikbs.taglib;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.*;


/** 
 * TODO Provide description 
 * 
 * @author NCS - updated by anthony kartasasmita 
 * @since 23/02/2011 
 */ 
public class ApplicationSearch extends TagSupport
{
    private static final Logger LOG = Logger.getLogger(ApplicationSearch.class);
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8750484923279099861L;

    public int doStartTag() throws JspException
    {

        ServletRequest request = pageContext.getRequest();
        String searchVal = request.getParameter("searchVal");
        String appSearch = request.getParameter("appSearch");

        String sel = null;

        if (searchVal != null && !searchVal.equals("") && appSearch != null && !appSearch.equals(""))
        {
            if (appSearch.equals("System Manager"))
            {
                String keyword[] = splitSearchVal(searchVal); 
                
                sel = "SELECT distinct a.ID, a.NAME FROM IKBS_APP a, IKBS_CONTACT c " +
                    "WHERE a.SYS_MGR = c.ID AND " +
                    "((c.S_FIRST_NAME || c.S_LAST_NAME) LIKE '%" + keyword[0] + "%" + keyword[1] + "%' " +
                     " OR " +
                     "(c.S_FIRST_NAME || c.S_LAST_NAME) LIKE '%" + keyword[1] + "%" + keyword[0] + "%' " + 
                     " OR " +
                     "(c.S_LAST_NAME || c.S_FIRST_NAME) LIKE '%" + keyword[0] + "%" + keyword[1] + "%' " + 
                     " OR " +
                     "(c.S_LAST_NAME || c.S_FIRST_NAME) LIKE '%" + keyword[1] + "%" + keyword[0] + "%' " +
                    ") ORDER BY a.NAME";
            }
            
            else if (appSearch.equals("Skillgroup"))
                sel = "SELECT distinct a.ID, a.NAME FROM IKBS_APP a, IKBS_SKG g, IKBS_APP_SKG ag " +
                		"WHERE a.ID = ag.APP_ID AND ag.SKG_ID = g.ID AND g.S_NAME LIKE '%" +
                		searchVal.toUpperCase() + "%' ORDER BY a.NAME";
            
            else if (appSearch.equals("Skillgroup Contact"))
            {
                String keyword[] = splitSearchVal(searchVal); 
                
                sel = "SELECT distinct a.ID, a.NAME FROM IKBS_APP a, IKBS_CONTACT c, IKBS_APP_CONTACT ac " +
                        "WHERE a.ID = ac.APP_ID AND ac.CONTACT_ID = c.ID AND " +
                        "((c.S_FIRST_NAME || c.S_LAST_NAME) LIKE '%" + keyword[0] + "%" + keyword[1] + "%' " +
                        " OR " +
                        "(c.S_FIRST_NAME || c.S_LAST_NAME) LIKE '%" + keyword[1] + "%" + keyword[0] + "%' " + 
                        " OR " +
                        "(c.S_LAST_NAME || c.S_FIRST_NAME) LIKE '%" + keyword[0] + "%" + keyword[1] + "%' " + 
                        " OR " +
                        "(c.S_LAST_NAME || c.S_FIRST_NAME) LIKE '%" + keyword[1] + "%" + keyword[0] + "%' " +
                       ") ORDER BY a.NAME";                
            }

            else if (appSearch.equals("Application"))
            {
                sel = "SELECT a.ID, a.NAME FROM IKBS_APP a WHERE (a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) LIKE '%"
                    + searchVal.toUpperCase() + "%' ORDER BY a.NAME";                
            }
            
            else if (appSearch.equals("Latest Applications"))
            {
                sel = "SELECT a.ID, a.NAME FROM IKBS_APP a WHERE rownum < 11 ORDER BY a.DT_MAJOR_UPD DESC";                
            }

            else if (appSearch.equals("Problem"))
            {
                String keyword[] = splitSearchVal(searchVal); 
                
                sel = "SELECT DISTINCT a.ID, a.NAME FROM IKBS_APP a, IKBS_LOGGING_PROC l "
                      + "where a.ID = l.APP_ID(+) and ((l.S_PROB_DESC || a.S_NAME || a.S_LONG_NAME || a.S_ALIAS) LIKE '%"
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
                      + "%' ) ORDER BY a.NAME";
            }
            else if (appSearch.equals("CommandCentre"))
            {
                String keyword[] = splitSearchVal(searchVal); 

                sel = "SELECT  DISTINCT a.ID, a.NAME FROM IKBS_APP a, IKBS_CC_FIX c "
                      + "where a.ID = c.APP_ID(+) and  ( (c.S_SYMPTOM || a.S_NAME) LIKE '%"
                      + keyword[0] + "%" + keyword[1] + "%" + keyword[2] 
                      + "%'"
                      + " OR "
                      + "(c.S_SYMPTOM || a.S_NAME) "
                      + "LIKE "
                      + "'%"
                      + keyword[0] + "%" + keyword[2] + "%" + keyword[1]
                      + "%'"
                      + " OR "
                      + "(c.S_SYMPTOM || a.S_NAME) "
                      + "LIKE "
                      + "'%"
                      + keyword[1] + "%" + keyword[0] + "%" + keyword[2]
                      + "%'"
                      + " OR "
                      + "(c.S_SYMPTOM || a.S_NAME) "
                      + "LIKE "
                      + "'%"
                      + keyword[1] + "%" + keyword[2] + "%" + keyword[0]
                      + "%'"
                      + " OR "
                      + "(c.S_SYMPTOM || a.S_NAME) "
                      + "LIKE "
                      + "'%"
                      + keyword[2] + "%" + keyword[0] + "%" + keyword[1]
                      + "%'"
                      + " OR "
                      + "(c.S_SYMPTOM || a.S_NAME) "
                      + "LIKE "
                      + "'%" + keyword[2] + "%" + keyword[1] + "%" + keyword[0] + "%' )" + " ORDER BY a.NAME";

            }else
            {
                throw new JspException("Unknown Search Type: " + searchVal);
            }
        }else if (appSearch.equals("Latest Applications"))
        {
            sel = "SELECT a.ID, a.NAME FROM IKBS_APP a WHERE rownum < 11 ORDER BY a.DT_MAJOR_UPD DESC";                
        }
        else
        {
            String firstLetter = request.getParameter("firstLetter");
            if (firstLetter == null) 
            {
                firstLetter = "A";                
            }
            
            sel = "SELECT ID, NAME FROM IKBS_APP WHERE S_NAME LIKE '" + firstLetter.toUpperCase() + "%' ORDER BY NAME";
        }

        LOG.debug("Search SQL: " + sel);
        
        ApplicationList.doSearch(pageContext, sel);

        return SKIP_BODY;
    }
    
    /**
     * Split the search values up to 3 keywords
     * 
     * @param searchVal input
     * @return String array
     * @since 24/02/2011
     */
    private String[] splitSearchVal(String searchVal) 
    {
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
        
        return keyword;
    }
}
