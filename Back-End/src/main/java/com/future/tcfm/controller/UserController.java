package com.future.tcfm.controller;

import com.future.tcfm.model.User;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> loadAll (){
        return userService.loadAll();
    }

    @GetMapping("/email")
    public User getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") String id, @RequestBody User user) {
        return userService.updateUser(id,user);
    }

}

