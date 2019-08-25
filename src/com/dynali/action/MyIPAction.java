package com.dynali.action;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class MyIPAction extends DynaliActionDefault {
    private String APIReferenceName = "myip";

    public MyIPAction() throws UnsupportedEncodingException {
        super.payload = new StringEntity("{\"action\":\"" + getAPIReferenceName() + "\"}");
    }
}
