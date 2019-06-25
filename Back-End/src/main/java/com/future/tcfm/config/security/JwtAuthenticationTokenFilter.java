package com.future.tcfm.config.security;
import com.future.tcfm.model.JwtAuthenticationToken;
import com.future.tcfm.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    public JwtAuthenticationTokenFilter() {
        super("/api/**");
    }

    @Value("${app.jwtSecret}")
    private String secretKey = "futureProgram";
    @Autowired
    private JwtValidator jwtValidator;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Token ")) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWTs is missing");
        }

        String authenticationToken = header.substring(6); // ambil nilai dari tokeno dimulai dari index ke 7
        JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);

        try { Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authenticationToken);
        } catch (JwtException ex) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage());
            return null;
        }
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        jwtValidator.onSuccessAuth(authResult.getName());
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"401 UNAUTHORIZED ACCESS");
    }




}