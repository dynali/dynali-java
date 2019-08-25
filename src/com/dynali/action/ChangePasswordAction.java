package com.dynali.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChangePasswordAction extends HostnameAction {
    public String APIreferenceName = "changepassword";
    public String newPassword; //TODO

    ChangePasswordAction() throws UnsupportedEncodingException {
    }

    public List<String> getValidationErrors() {
        List<String> validationErrors = super.getValidationErrors();

        if (isPatternNotMatching(password, "^[a-f0-9]{32}$")) {
            validationErrors.add("Invalid or missing password.");
        }

        return validationErrors;
    }
}
