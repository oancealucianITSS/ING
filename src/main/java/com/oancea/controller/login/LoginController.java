package com.oancea.controller.login;

import com.oancea.dto.request.UserLoginRequest;
import com.oancea.dto.response.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface LoginController {

    @PostMapping(value = "/login",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Response> login(@Valid @RequestBody UserLoginRequest userLoginRequest);

}