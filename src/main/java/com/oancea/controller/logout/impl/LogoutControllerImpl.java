package com.oancea.controller.logout.impl;

import com.oancea.controller.logout.LogoutController;
import com.oancea.dto.request.UserLogoutRequest;
import com.oancea.dto.response.Response;
import com.oancea.service.logout.LogoutService;
import com.oancea.utils.SensitiveLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.oancea.utils.Constants.*;

@RestController
public class LogoutControllerImpl implements LogoutController {

    private static final String LOGOUT_USER_METHOD = "logout";

    private static final String LOGOUT_USER_PATH = "/logout";

    private static final Logger logger = LoggerFactory.getLogger(LogoutControllerImpl.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private LogoutService logoutService;

    @Override
    public ResponseEntity<Response> logout(UserLogoutRequest userLogoutRequest) {
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);
        sensitiveLog.logSensitive(userLogoutRequest.getEmail(), logIdentifiers, LOGOUT_USER_PATH, LOGOUT_USER_METHOD, logger, null, null);

        Response response = logoutService.logout(userLogoutRequest);

        logIdentifiers.replace(TYPE, OUTPUT);
        sensitiveLog.logSensitive(response, logIdentifiers, LOGOUT_USER_PATH, LOGOUT_USER_METHOD, logger, response.getMessageStatus(), response.getStatus());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(
            MethodArgumentNotValidException ex) {
        Response response = new Response();
        response.setMessageStatus(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(400).body(response);
    }
}
