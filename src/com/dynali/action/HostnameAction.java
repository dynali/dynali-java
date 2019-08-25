package com.dynali.action;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HostnameAction extends DynaliActionDefault implements DynaliAction {
    public String hostname;
    public String username;
    public String password;

    HostnameAction() throws UnsupportedEncodingException {
    }

    public static String getMD5Hash(String input) {
        return DigestUtils.md5Hex(input).toUpperCase();
    }

    public boolean isPatternNotMatching(String aString, String pattern) {
        Pattern aPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher aMatcher = aPattern.matcher(aString);
        return !aMatcher.find();
    }

    public List<String> getValidationErrors() {
        List<String> validationErrors = super.getValidationErrors();

        if (isPatternNotMatching(hostname, "^([a-z0-9\\-]+\\.)+[a-z]+$")) {
            validationErrors.add("Invalid or missing hostname.");
        }
        if (isPatternNotMatching(username, "[a-z0-9]{4,128}$")) {
            validationErrors.add("Invalid or missing username.");
        }
        if (isPatternNotMatching(password, "^[a-f0-9]{32}$")) {
            validationErrors.add("Invalid or missing password.");
        }

        return validationErrors;
    }

}
