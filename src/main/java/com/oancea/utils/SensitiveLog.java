package com.oancea.utils;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("sensitiveLog")
public class SensitiveLog {

    private static final String SENSITIVE_TEMPLATE = "Identifier: [{}]: Logging => [\n"
            + "  URI : {} \n"
            + "  Method : {} \n"
            + "  MessageStatus : {} \n"
            + "  Status : {} \n"
            + "  ObjectType : {} \n"
            + "  Data: {} \n"
            + "] \n";

//    We should use SENSITIVE_PLACEHOLDER for sensitive data
//    public static final String SENSITIVE_PLACEHOLDER = "XXXXXXXXXXX";

    public void logSensitive(Object obj, Map<String, String> logIdentifiers, String path, String method, Logger log, String messageStatus, Integer status) {
        //Identifiers will contain dinamic data representing the context of the log (userId/username + value, role + value etc)
        //Identifiers should not contain sensitive data when computed
        log.debug(SENSITIVE_TEMPLATE, logIdentifiers.toString(), path, method, messageStatus, status, obj.getClass(), obj.toString());
    }
}
