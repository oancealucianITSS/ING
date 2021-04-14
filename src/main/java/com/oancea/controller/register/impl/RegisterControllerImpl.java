package com.oancea.controller.register.impl;

import com.oancea.controller.register.RegisterController;
import com.oancea.dto.request.UserRegisterRequest;
import com.oancea.dto.response.ResponseCreateUser;
import com.oancea.service.register.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterControllerImpl implements RegisterController {

    @Autowired
    private RegisterService registerService;

    @Override
    public ResponseEntity<ResponseCreateUser> createUser(UserRegisterRequest userRegisterRequest) {

        ResponseCreateUser response = registerService.createUser(userRegisterRequest);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
