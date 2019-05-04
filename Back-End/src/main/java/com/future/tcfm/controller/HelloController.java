package com.future.tcfm.controller;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity hello(){
        List<User> userList = userRepository.findAll();
        return new ResponseEntity<List> (userList, null ,HttpStatus.OK);
    }
}
