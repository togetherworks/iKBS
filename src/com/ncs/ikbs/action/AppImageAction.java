package com.ncs.ikbs.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ncs.iframe.extensions.action.BaseFlowCommandAction;
import com.ncs.ikbs.util.NoImageStream;
import com.ncs.ikbs.util.Transaction;

public class AppImageAction extends BaseFlowCommandAction {

    public AppImageAction() {
        super();
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    		throws SQLException, IOException {
        
        response.setHeader("Content-Type", "image/jpeg");

        String appIdStr = request.getParameter("applicationId");
        byte[] bytes = null;

        if(appIdStr != null) {

            // Get image bytes
            int appId = Integer.parseInt(appIdStr);
            bytes = getImage(appId);
        }
        
        if(bytes == null) {
            bytes = getNoImage();
        }
        
        // Write bytes
        OutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
        
        return null;
    }
    
    private byte[] getImage(int applicationId)
    		throws SQLException {
        
        Transaction transaction = new Transaction();
        Connection con = transaction.getConnection();
        byte[] bytes = null;

        try {
            String sel = "SELECT IMAGE FROM KBS_IMAGE WHERE APPID = ?";
            PreparedStatement p = con.prepareStatement(sel);
            p.setInt(1, applicationId);
            ResultSet rs = p.executeQuery();
            
            InputStream is = null;

            if(rs.next()) {
                is = rs.getBinaryStream(1);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	            int c;
	
	            while((c = is.read()) != -1) {
	                baos.write(c);
	            }
	
	            bytes = baos.toByteArray();
	
	            baos.close();
	            is.close();
            }
            
            rs.close();
            p.close();
            con.close();

            return bytes;

        } catch (IOException e) {
            e.printStackTrace();
            throw new SQLException("IOException: " + e);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;

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
    
    private byte[] getNoImage() 
    		throws IOException {
        
        byte[] bytes = null;
        
        InputStream is = NoImageStream.getNoImageStream();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int c;

        while((c = is.read()) != -1) {
            baos.write(c);
        }

        bytes = baos.toByteArray();

        baos.close();
        is.close();
        
        return bytes;
    }
}
