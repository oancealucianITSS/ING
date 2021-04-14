package com.oancea.controller.login.impl;

import com.oancea.controller.login.LoginController;
import com.oancea.dto.request.UserLoginRequest;
import com.oancea.dto.response.Response;
import com.oancea.service.login.LoginService;
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
public class LoginControllerImpl implements LoginController {

    private static final String LOGIN_USER_METHOD = "login";

    private static final String LOGIN_USER_PATH = "/login";

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerImpl.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private LoginService loginService;

    @Override
    public ResponseEntity<Response> login(UserLoginRequest userLoginRequest) {
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);
        logIdentifiers.put(EMAIL, userLoginRequest.getEmail());

        sensitiveLog.logSensitive("", logIdentifiers, LOGIN_USER_PATH, LOGIN_USER_METHOD, logger, null, null);

        Response response = loginService.login(userLoginRequest);

        logIdentifiers.replace(TYPE, OUTPUT);
        sensitiveLog.logSensitive(response, logIdentifiers, LOGIN_USER_PATH, LOGIN_USER_METHOD, logger,
                response.getMessageStatus(), response.getStatus());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException ex) {
        Response response = new Response();
        response.setMessageStatus(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(400).body(response);
    }
}
