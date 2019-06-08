package com.future.tcfm.service;

import com.future.tcfm.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<User> loadAll();
    User getUser(String email);
    ResponseEntity createUser(User user);
    ResponseEntity updateUser(String id, User user);
//    ResponseEntity deleteUser(String id);
}
