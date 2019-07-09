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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtGenerator {
    @Value("${app.jwtSecret}")
    private static String secretKey="futureProgram";

    @Value("${app.jwtExpirationInMs}")
    private static Long jwtExpirationInMs = 180000L;

    @Value("${app.jwtExpirationInMs}")
    private static Long refreshTokenExpirationInMs = 1800000L;
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
                .setExpiration(new Date(System.currentTimeMillis()+9999999999999999L))//development phase token last for days
//                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String generateRefreshToken(String id){
        return UUID.randomUUID().toString()+id;
    }

    /**
     * loginHandler below
     *
     * @param loginRequest
     * @return
     */
    public ResponseEntity loginResponse(LoginRequest loginRequest){
        System.out.println(loginRequest);
        User userExist = userRepository.findByEmail(loginRequest.getEmail());
        if (userExist!=null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), userExist.getPassword())){
                Map<String,String> tokenMap = new HashMap<>();
                String refreshToken = generateRefreshToken(userExist.getIdUser());
                String token = generateToken(userExist.getEmail());
                tokenMap.put("token",token);
                tokenMap.put("refreshToken",refreshToken);
                tokenMap.put("role",userExist.getRole());
                tokenMap.put("groupName",userExist.getGroupName());
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userExist.getRole());
                JwtUserDetails jwtUserDetails = jwtUserDetailsRepository.findByEmail(loginRequest.getEmail());
                if(jwtUserDetails== null)
                    jwtUserDetails = new JwtUserDetails();
                jwtUserDetails.setEmail(userExist.getEmail());
                jwtUserDetails.setToken(tokenMap.get("token"));
                jwtUserDetails.setRefreshToken(tokenMap.get("refreshToken"));
                jwtUserDetails.setRefreshTokenExpiredAt(new Date().getTime()+refreshTokenExpirationInMs);
                jwtUserDetails.setAuthorities(grantedAuthorities);
                jwtUserDetails.setGroupName(userExist.getGroupName());
                jwtUserDetailsRepository.save(jwtUserDetails);
                return new ResponseEntity(tokenMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
    }
}