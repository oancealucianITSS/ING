package com.oancea.service.register.impl;

import com.oancea.domain.UserEntity;
import com.oancea.dto.UserDto;
import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;
import com.oancea.mapper.UserDTOToEntityMapper;
import com.oancea.mapper.UserEntityToDTOMapper;
import com.oancea.repository.UserRepository;
import com.oancea.service.register.RegisterService;
import com.oancea.utils.EmailValidator;
import com.oancea.utils.SensitiveLog;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.oancea.utils.Constants.*;

@Service
public class RegisterServiceImpl implements RegisterService {

    private static final String FIRST_NAME = "First name ";

    private static final String LAST_NAME = "Last name ";

    private static final String USER_REQUEST = "UserRegisterRequest  : ";

    public static final String IS_ACTIVE = "Is active";

    public static final String INVALID_EMAIL_ADRESS = "INVALID_EMAIL_ADRESS";

    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private UserEntityToDTOMapper userEntityToDTOMapper = Mappers.getMapper(UserEntityToDTOMapper.class);

    private UserDTOToEntityMapper userRequesToEntityMapper = Mappers.getMapper(UserDTOToEntityMapper.class);

    @Autowired
    private SensitiveLog sensitiveLog;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseCreateUser createUser(UserRegisterRequest userRegisterRequest) {
        ResponseCreateUser response;
        Map<String, String> logIdentifiers = new HashMap<>();
        logIdentifiers.put(TYPE, INPUT);

        try {
            inputLogIdentifiers(userRegisterRequest, logIdentifiers);
            logIdentifiers.replace(TYPE, OUTPUT);
            // check email validation
            boolean isEmailValid = EmailValidator.checkEmail(userRegisterRequest.getEmail());
            if (!isEmailValid) {
                response = new ResponseCreateUser(400, INVALID_EMAIL_ADRESS, null);

                sensitiveLog.logSensitive("", logIdentifiers, null, null, LOGGER, INVALID_EMAIL_ADRESS, 400);
                return response;
            }
            Optional<UserEntity> userEntity = userRepository.findByEmail(userRegisterRequest.getEmail().toLowerCase());
            if (userEntity.isPresent()) {
                response = new ResponseCreateUser(409, USER_ALREADY_EXISTS, null);

                sensitiveLog.logSensitive("", logIdentifiers, null, null, LOGGER, USER_ALREADY_EXISTS, 409);
                return response;
            } else {
                UserEntity userEntityToSave = userRequesToEntityMapper.mapUserDTOToEntity(userRegisterRequest);
                UserEntity userEntitySaved = userRepository.save(userEntityToSave);
                UserDto userDto = userEntityToDTOMapper.mapUserEntityToDTO(userEntitySaved);
                response = new ResponseCreateUser(200, SUCCESS, userDto);

                sensitiveLog.logSensitive("", logIdentifiers, null, null, LOGGER, SUCCESS, 200);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseCreateUser(500, INTERNAL_SERVER_ERROR, null);

            logIdentifiers.replace(TYPE, ERROR);
            sensitiveLog.logSensitive(ERROR_MESSAGE + e.getMessage(), logIdentifiers, null, null, LOGGER,
                    INTERNAL_SERVER_ERROR, 500);
            return response;
        }
    }

    @Override
    public void inputLogIdentifiers(UserRegisterRequest userRegisterRequest, Map<String, String> logIdentifiers) {
        logIdentifiers.put(EMAIL, userRegisterRequest.getEmail());
        logIdentifiers.put(FIRST_NAME, userRegisterRequest.getFirstName());
        logIdentifiers.put(LAST_NAME, userRegisterRequest.getLastName());
        logIdentifiers.put(IS_ACTIVE, userRegisterRequest.getIsActive().toString());
        sensitiveLog.logSensitive(USER_REQUEST, logIdentifiers, null, null, LOGGER, null, null);
    }

}
