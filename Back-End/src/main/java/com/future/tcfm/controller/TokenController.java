package com.future.tcfm.controller;

import com.future.tcfm.config.security.JwtGenerator;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    UserRepository userRepository;

    private JwtGenerator jwtGenerator;

    public TokenController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    public String generate(@RequestBody final User user) {
        User userExist = userRepository.findByEmail(user.getEmail());
        if (userExist!=null)
            return jwtGenerator.generate(user);
        else
            throw new RuntimeException("User Not Found");
    }
}
