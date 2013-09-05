<%@ page import="java.util.*,java.io.*"%>
<%
  String filePath = this.getServletContext().getRealPath("/") + "img/techref/";
  String fileName = "EscalationList.xls";

  File downloadFile = new File(filePath+fileName);

  System.out.println("File " + fileName);

  try {
      if (downloadFile.exists()) {
        //set response headers to force download
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=\""+ fileName + "\"");
          
        //stream out file
        FileInputStream fInputStream =new FileInputStream(filePath+fileName);
        ServletOutputStream fOutputStream = response.getOutputStream();
        int i;
        while ((i=fInputStream.read()) != -1) {
            fOutputStream.write(i);
        }
        fInputStream.close();
        fOutputStream.close();
      }
      else {
            System.out.println("File not found @"+filePath+fileName);
      }
  } catch (Exception exp){
     System.out.println("Exception: "+exp.getMessage());
  }
%>