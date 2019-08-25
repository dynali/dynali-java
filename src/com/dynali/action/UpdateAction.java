package com.dynali.action;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UpdateAction extends HostnameAction {
    public String APIReferenceName = "update";
    public String IP; //TODO

    UpdateAction() throws UnsupportedEncodingException {
    }

    private boolean validateIPv4(String IPString) {
        if (isNullOrWhitespace(IPString)) {
            return false;
        }

        String[] splitValues = IPString.split(".");
        if (splitValues.length != 4) {
            return false;
        }

        for (String s : splitValues) {
            try {
                Byte.parseByte(s);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNullOrWhitespace(String s) {
        if (s == null)
            return true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public List<String> getValidationErrors() {
        List<String> validationErrors = super.getValidationErrors();

        if (!Objects.equals(IP, "auto") && !validateIPv4(IP)) {
            validationErrors.add("Invalid IP. Provided `" + IP + "`.");
        }

        return validationErrors;
    }

}
