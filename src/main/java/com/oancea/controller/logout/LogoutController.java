package com.oancea.controller.logout;

import com.oancea.dto.request.UserLogoutRequest;
import com.oancea.dto.response.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface LogoutController {
    @PostMapping(value = "/logout",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Response> logout(@Valid @RequestBody UserLogoutRequest userLogoutRequest);
}
