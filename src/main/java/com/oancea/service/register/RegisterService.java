package com.oancea.service.register;

import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;

public interface RegisterService {
    ResponseCreateUser createUser(UserRegisterRequest userRegisterRequest);
}
