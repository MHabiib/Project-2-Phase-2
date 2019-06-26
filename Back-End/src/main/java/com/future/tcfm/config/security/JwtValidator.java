package com.future.tcfm.config.security;

import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import com.future.tcfm.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class JwtValidator {

    @Value("${app.jwtSecret}")
    private String secretKey;


    @Value("${app.jwtExpirationInMs}")
    private Long refreshTokenExpirationInMs;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtUserDetailsRepository jwtUserDetailsRepository;

    @Autowired
    private UserRepository userRepository;
    public JwtUserDetails validate(String token) {

        JwtUserDetails jwtUserDetails= null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            jwtUserDetails = jwtUserDetailsRepository.findByEmail(body.getSubject());
        } catch (JwtException e){
            throw new JwtException(e.getMessage());
        }
        return jwtUserDetails;
    }

    public String onSuccessAuth(String email){
        JwtUserDetails currentUser = jwtUserDetailsRepository.findByEmail(email);
        if(currentUser == null) throw new RuntimeException("Current user is null!");
        String newToken =  jwtGenerator.generateToken(email);
        currentUser.setToken(newToken);
        currentUser.setRefreshTokenExpiredAt(System.currentTimeMillis()+refreshTokenExpirationInMs);
        jwtUserDetailsRepository.save(currentUser);
        System.out.println("Refresh token expired at : "+ currentUser.getRefreshTokenExpiredAt());
        return newToken;
//        return null;
    }
    public ResponseEntity getRefreshToken(String token, String refreshToken){
        System.out.println(token+"_"+refreshToken);
        JwtUserDetails jwtUserDetails = jwtUserDetailsRepository.findByTokenAndRefreshToken(token,refreshToken);
        if(jwtUserDetails==null){
            return new ResponseEntity("404 RefreshToken not found", HttpStatus.NOT_FOUND);
        }
        String newToken = jwtGenerator.generateToken(jwtUserDetails.getEmail());
        String newRefreshToken = jwtGenerator.generateRefreshToken(jwtUserDetails.getId());
        jwtUserDetails.setToken(newToken);
        jwtUserDetails.setRefreshToken(newRefreshToken);
        jwtUserDetails.setRefreshTokenExpiredAt(new Date().getTime()+refreshTokenExpirationInMs);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",newToken);
        tokenMap.put("refreshToken",newRefreshToken);
        jwtUserDetailsRepository.save(jwtUserDetails);
        return new ResponseEntity(tokenMap,HttpStatus.OK);
    }

}