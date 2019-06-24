package com.future.tcfm.config.security;

import com.future.tcfm.model.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtSuccessHandler implements AuthenticationSuccessHandler{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println("Successfully Authentication");
        System.out.println("User ID : "+((JwtUserDetails)authentication.getPrincipal()).getId());
        System.out.println("Email/Username : " +((JwtUserDetails)authentication.getPrincipal()).getUserName());
        System.out.println("Password: " +((JwtUserDetails)authentication.getPrincipal()).getPassword());
        System.out.println("Authorities: " +((JwtUserDetails)authentication.getPrincipal()).getAuthorities());
        System.out.println("Access token : " +((JwtUserDetails)authentication.getPrincipal()).getToken());


//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//
//             username = ((JwtUserDetails)principal).getToken();
//
//        } else {
//
//             username = principal.toString();
//
//        }
//        System.out.println("username : "+username);


    }
}