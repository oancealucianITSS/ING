package com.oancea.service.logout;

import com.oancea.dto.request.UserLogoutRequest;
import com.oancea.dto.response.Response;

public interface LogoutService {
    Response logout(UserLogoutRequest userLogoutRequest);
}
