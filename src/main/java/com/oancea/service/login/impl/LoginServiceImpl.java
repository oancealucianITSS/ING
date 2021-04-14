package com.oancea.service.login.impl;

import com.oancea.domain.UserEntity;
import com.oancea.dto.request.UserLoginRequest;
import com.oancea.dto.response.Response;
import com.oancea.repository.UserRepository;
import com.oancea.service.login.LoginService;
import com.oancea.utils.EmailValidator;
import com.oancea.utils.SensitiveLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.oancea.utils.Constants.*;

@Service
public class LoginServiceImpl implements LoginService {

    public static final String INVALID_EMAIL_ADRESS = "INVALID_EMAIL_ADRESS";

    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";

    public static final String WRONG_EMAIL = "WRONG_EMAIL";

    public static final String USER_LOGIN_REQUEST = "UserLoginRequest";

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response login(UserLoginRequest userLoginRequest) {
        Response response;
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);
        logIdentifiers.put(EMAIL, userLoginRequest.getEmail());

        try {
            sensitiveLog.logSensitive(USER_LOGIN_REQUEST, logIdentifiers, null, null, logger, null, null);
            logIdentifiers.replace(TYPE, OUTPUT);

            boolean isEmailValid = EmailValidator.checkEmail(userLoginRequest.getEmail());
            if (!isEmailValid) {
                response = new Response(400, INVALID_EMAIL_ADRESS);

                sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, INVALID_EMAIL_ADRESS, 400);
                return response;
            } else {
                Optional<UserEntity> userByEmail = userRepository.findByEmail(userLoginRequest.getEmail());
                if (userByEmail.isPresent()) {
                    if (userByEmail.get().getPassword().equals(userLoginRequest.getPassword())) {
                        userByEmail.get().setIsActive(true);
                        userRepository.save(userByEmail.get());
                        response = new Response(200, SUCCESS);

                        sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, SUCCESS, 200);
                        return response;
                    } else {
                        response = new Response(401, WRONG_PASSWORD);

                        sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, WRONG_PASSWORD, 401);
                        return response;
                    }
                } else {
                    response = new Response(401, WRONG_EMAIL);

                    sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, WRONG_EMAIL, 401);
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logIdentifiers.replace(TYPE, ERROR);
            response = new Response(500, e.getMessage());

            sensitiveLog.logSensitive(ERROR_MESSAGE + e.getMessage(), logIdentifiers, null, null, logger, e.getMessage(), 500);
            return response;
        }
    }
}
