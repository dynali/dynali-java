package com.dynali.action;

import com.dynali.DynaliException;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class DynaliActionDefault implements DynaliAction {
    StringEntity payload;
    private String APIReferenceName = "";
    private List<String> validationErrors = new ArrayList<>();

    DynaliActionDefault() throws UnsupportedEncodingException {
    }

    public final StringEntity toJson() throws DynaliException {
        if (APIReferenceName.length() == 0) {
            throw new DynaliException(-982, "Invalid or missing APIReferenceName");
        }
        return payload;
    }

    String getAPIReferenceName() {
        return APIReferenceName;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

}
