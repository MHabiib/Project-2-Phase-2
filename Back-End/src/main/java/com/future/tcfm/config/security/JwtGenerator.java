package com.future.tcfm.config.security;

import com.future.tcfm.model.ReqResModel.LoginRequest;
import com.future.tcfm.model.ReqResModel.TokenResponse;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtGenerator {
    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generate(User user) {
        Claims claims = Jwts.claims()
                .setSubject(user.getEmail());
        claims.put("password", user.getPassword());
        claims.put("role",user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String generateRefreshToken(){
        return UUID.randomUUID().toString();
    }

    public ResponseEntity tokenResponse(LoginRequest loginRequest){
        System.out.println(loginRequest);
        User userExist = userRepository.findByEmail(loginRequest.getEmail());
        if (userExist!=null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), userExist.getPassword())){
                TokenResponse tokenResponse =  TokenResponse.builder()
                        .accessToken(generate(userExist))
                        .refreshToken(generateRefreshToken()).build();
                return new ResponseEntity(tokenResponse, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
    }
}