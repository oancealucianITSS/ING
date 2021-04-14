package com.oancea.service.register;

import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;

import java.util.Map;

public interface RegisterService {
    ResponseCreateUser createUser(UserRegisterRequest userRegisterRequest);
    void inputLogIdentifiers(UserRegisterRequest userRegisterRequest, Map<String, String> logIdentifiers);
}
