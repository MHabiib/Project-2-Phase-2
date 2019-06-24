package com.future.tcfm.controller;

import com.future.tcfm.config.security.JwtAuthenticationProvider;
import com.future.tcfm.config.security.JwtGenerator;
import com.future.tcfm.model.ReqResModel.LoginRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping(value = "/signin",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generate(@RequestBody final LoginRequest loginRequest) {
        return jwtGenerator.tokenResponse(loginRequest);
    }
}
