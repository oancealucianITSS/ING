package com.oancea.service.logout.impl;

import com.oancea.domain.UserEntity;
import com.oancea.dto.request.UserLogoutRequest;
import com.oancea.dto.response.Response;
import com.oancea.repository.UserRepository;
import com.oancea.service.logout.LogoutService;
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
public class LogoutServiceImpl implements LogoutService {

    public static final String INVALID_EMAIL_ADRESS = "INVALID_EMAIL_ADRESS";

    public static final String USER_DOES_NOT_EXIST = "USER_DOES_NOT_EXIST";

    public static final String USER_LOGOUT_REQUEST = "UserLogoutRequest";

    private static final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response logout(UserLogoutRequest userLogoutRequest) {
        Response response;
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);
        logIdentifiers.put(EMAIL, userLogoutRequest.getEmail());
        sensitiveLog.logSensitive(USER_LOGOUT_REQUEST, logIdentifiers, null, null, logger, null, null);
        logIdentifiers.put(TYPE, OUTPUT);
        try {
            boolean isEmailValid = EmailValidator.checkEmail(userLogoutRequest.getEmail());
            if (!isEmailValid) {
                response = new Response(400, INVALID_EMAIL_ADRESS);

                sensitiveLog.logSensitive(USER_LOGOUT_REQUEST, logIdentifiers, null, null, logger, INVALID_EMAIL_ADRESS, 400);
                return response;
            } else {
                Optional<UserEntity> userByEmail = userRepository.findByEmail(userLogoutRequest.getEmail());
                if (userByEmail.isPresent()) {
                    userByEmail.get().setIsActive(false);
                    userRepository.save(userByEmail.get());
                    response = new Response(200, SUCCESS);

                    sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, SUCCESS, 200);
                    return response;
                } else {
                    response = new Response(401, USER_DOES_NOT_EXIST);

                    sensitiveLog.logSensitive("", logIdentifiers, null, null, logger, INTERNAL_SERVER_ERROR, 500);
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
