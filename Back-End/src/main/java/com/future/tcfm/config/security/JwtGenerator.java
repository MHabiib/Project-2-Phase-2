package com.future.tcfm.config.security;

import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.model.ReqResModel.LoginRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import com.future.tcfm.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtGenerator {
    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    @Value("${app.jwtExpirationInMs}")
    private Long refreshTokenExpirationInMs;
    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUserDetailsRepository jwtUserDetailsRepository;

    public String generateToken(String email) {
        Claims claims = Jwts.claims()
                .setSubject(email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+9999999999999L))
//                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String generateRefreshToken(String userId){
        return UUID.randomUUID().toString()+userId;
    }

    public ResponseEntity tokenResponse(LoginRequest loginRequest){
        System.out.println(loginRequest);
        User userExist = userRepository.findByEmail(loginRequest.getEmail());
        if (userExist!=null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), userExist.getPassword())){
                Map<String,String> tokenMap = new HashMap<>();
                String refreshToken = generateRefreshToken(userExist.getIdUser());
                String token = generateToken(userExist.getEmail());
                tokenMap.put("token",token);
                tokenMap.put("refreshToken",refreshToken);

                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(userExist.getRole());
                JwtUserDetails jwtUserDetails = new JwtUserDetails(
                        userExist.getEmail(),
                        tokenMap.get("token"),
                        tokenMap.get("accessToken"),
                        new Date().getTime()+refreshTokenExpirationInMs,
                        grantedAuthorities
                );
                jwtUserDetailsRepository.save(jwtUserDetails);
                return new ResponseEntity(tokenMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
    }
}