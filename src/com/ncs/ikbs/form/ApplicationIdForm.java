package com.ncs.ikbs.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ApplicationIdForm extends ValidatorForm {

    private int applicationId;

    public ApplicationIdForm() {
        super();
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        applicationId = 0;
    }

    
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getApplicationId() {
        return applicationId;
    }
}
