package com.future.tcfm.config.security;

import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import com.future.tcfm.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class JwtValidator {

    @Value("${app.jwtSecret}")
    private String secretKey;


    @Value("${app.jwtExpirationInMs}")
    private Long refreshTokenExpirationInMs;

    @Autowired
    private JwtUserDetailsRepository jwtUserDetailsRepository;

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

    public void onSuccessAuth(String email){
        JwtUserDetails currentUser =  jwtUserDetailsRepository.findByEmail(email);
        currentUser.setRefreshTokenExpiredAt(System.currentTimeMillis()+refreshTokenExpirationInMs);
        jwtUserDetailsRepository.save(currentUser);
        System.out.println("Refresh token expired at : "+ currentUser.getRefreshTokenExpiredAt());
    }
}