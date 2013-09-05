package com.ncs.ikbs.util;

public class HTMLTool {

	public static String cleanString(String str)
    {
        if(str != null)
        {
            str = str.replaceAll("'", "&#146").trim();
            if(str.equals(""))
                str = "&nbsp;";
            return str;
        } else
        {
            return "&nbsp;";
        }
    }
}
