package com.future.tcfm.service;

import com.future.tcfm.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    List<User> loadAll();
    ResponseEntity createUser(User user);
    ResponseEntity updateUser(@PathVariable("id") String id, @RequestBody User user);
//    ResponseEntity deleteUser(@PathVariable("id") String id);
}
