package com.future.tcfm.controller;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @Autowired
    UserRepository userRepository;

    @GetMapping(value="", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity hello(){
        List<User> userList = userRepository.findAll();
        return new ResponseEntity<> (userList, null ,HttpStatus.OK);
    }
}
