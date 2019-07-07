package com.future.tcfm.config.security;

import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.future.tcfm.config.SecurityConfig.getCurrentUser;

public class JwtSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        System.out.println("Successfully Authentication");
        System.out.println("Email/Username : " + getCurrentUser().getEmail());
        System.out.println("Authorities: " +((JwtUserDetails)authentication.getPrincipal()).getAuthorities());
        System.out.println("Access token : " +((JwtUserDetails)authentication.getPrincipal()).getToken());
    }
}