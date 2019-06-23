package com.future.tcfm.config.security;

import com.future.tcfm.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtValidator {

    @Value("${app.jwtSecret}")
    private String secretKey;

    public User validate(String token) {

        User user= null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();
            user.setEmail(body.getSubject());
            user.setPassword((String) body.get("password"));
            user.setRole((String)body.get("role"));
        } catch (SignatureException ex) {
            throw new RuntimeException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("JWT Claims String is empty");
        }
        return user;
    }
}