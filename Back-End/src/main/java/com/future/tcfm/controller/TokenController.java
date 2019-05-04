package com.future.tcfm.controller;

import com.future.tcfm.config.security.JwtGenerator;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JwtGenerator jwtGenerator;

    public TokenController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    public String generate(@RequestBody final User user) {
        User userExist = userRepository.findByEmail(user.getEmail());
        if (userExist!=null)
            if (!passwordEncoder.matches(user.getPassword(),userExist.getPassword()))
                throw new RuntimeException("Wrong Password");
            else
                return jwtGenerator.generate(user);
        else
            throw new RuntimeException("User Not Found");
    }
}
