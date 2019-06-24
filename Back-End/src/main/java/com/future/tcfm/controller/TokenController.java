//package com.future.tcfm.controller;
//
//import com.future.tcfm.config.security.JwtAuthenticationProvider;
//import com.future.tcfm.config.security.JwtGenerator;
//import com.future.tcfm.model.User;
//import com.future.tcfm.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.print.attribute.standard.Media;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/token")
//public class TokenController {
//
//    @Autowired
//    private JwtAuthenticationProvider authenticationProvider;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private JwtGenerator jwtGenerator;
//
//    public TokenController(JwtGenerator jwtGenerator) {
//        this.jwtGenerator = jwtGenerator;
//    }
//
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public String generate(@RequestBody final User user) {
////        System.out.println(user);
//        User userExist = userRepository.findByEmail(user.getEmail());
//        if (userExist!=null)
//            if (!passwordEncoder.matches(user.getPassword(),userExist.getPassword()))
//                throw new RuntimeException("Wrong Password");
//            else {
//                return jwtGenerator.generate(userExist);
//
//            }
//        else
//            throw new RuntimeException("User Not Found");
//    }
//}
