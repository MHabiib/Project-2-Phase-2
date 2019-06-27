package com.future.tcfm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.config.security.JwtAuthenticationProvider;
import com.future.tcfm.config.security.JwtGenerator;
import com.future.tcfm.config.security.JwtValidator;
import com.future.tcfm.model.ReqResModel.LoginRequest;
import com.future.tcfm.model.ReqResModel.TokenResponse;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtValidator jwtValidator;

    @PostMapping(value = "/signin",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signIn(@RequestBody final LoginRequest loginRequest) {
        return jwtGenerator.tokenResponse(loginRequest);
    }
    @PostMapping(value = "/refreshtoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRefreshToken(
            @RequestBody TokenResponse tokenResponse
            ) {
        return jwtValidator.getRefreshToken(tokenResponse.getToken(),tokenResponse.getRefreshToken());
    }
    @DeleteMapping(value = "/signout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signOut(@RequestBody TokenResponse tokenResponse){
        return jwtValidator.signOut(tokenResponse.getToken(),tokenResponse.getRefreshToken());
    }
}
