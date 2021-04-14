package com.oancea.service.login;

import com.oancea.dto.request.UserLoginRequest;
import com.oancea.dto.response.Response;

public interface LoginService {
    Response login(UserLoginRequest userLoginRequest);
}
