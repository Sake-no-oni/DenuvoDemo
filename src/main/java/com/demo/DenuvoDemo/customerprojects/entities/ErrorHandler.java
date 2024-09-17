package com.demo.DenuvoDemo.customerprojects.entities;

import java.util.Map;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;

/**
 *
 * @author Andrii Filimonov
 */

public class ErrorHandler extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        Throwable error = getError(webRequest);
        if (error != null) {
            errorAttributes.put("message", error.getMessage());
        }

        return errorAttributes;
    }
}
