package com.dynali.action;

import com.dynali.DynaliException;
import org.apache.http.entity.StringEntity;

import java.util.List;

public interface DynaliAction {

    String APIReferenceName = "";

    StringEntity toJson() throws DynaliException;

    List<String> getValidationErrors();

}