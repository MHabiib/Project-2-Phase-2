package com.future.tcfm.config.security;

import com.future.tcfm.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;


@Component
public class JwtValidator {


    private String secret = "futureProgram";

    public User validate(String token) {

        User user= null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();
            user.setEmail(body.getSubject());
            user.setPassword((String) body.get("password"));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }
}