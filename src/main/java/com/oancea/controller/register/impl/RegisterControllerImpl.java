package com.oancea.controller.register.impl;

import com.oancea.controller.register.RegisterController;
import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;
import com.oancea.service.register.RegisterService;
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
public class RegisterControllerImpl implements RegisterController {

    private static final String CREATE_USER_METHOD = "createUser";

    private static final String CREATE_USER_PATH = "/createUser";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterControllerImpl.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private RegisterService registerService;

    @Override
    public ResponseEntity<ResponseCreateUser> createUser(UserRegisterRequest userRegisterRequest) {
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);
        registerService.inputLogIdentifiers(userRegisterRequest, logIdentifiers);
        sensitiveLog.logSensitive(userRegisterRequest, logIdentifiers, CREATE_USER_PATH, CREATE_USER_METHOD, LOGGER, null, null);

        ResponseCreateUser response = registerService.createUser(userRegisterRequest);

        logIdentifiers.replace(TYPE, OUTPUT);
        sensitiveLog.logSensitive(response, logIdentifiers, CREATE_USER_PATH, CREATE_USER_METHOD, LOGGER,
                response.getMessageStatus(), response.getStatus());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseCreateUser> handleValidationException(
            MethodArgumentNotValidException ex) {
        ResponseCreateUser response = new ResponseCreateUser();
        response.setPayload(null);
        response.setMessageStatus(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(400).body(response);
    }
}

