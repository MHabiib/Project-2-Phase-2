package com.future.tcfm.service;

import com.future.tcfm.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    List<User> loadAll();

    User createUser(User user);

    ResponseEntity<User> updateduser(@PathVariable("id") int id, @RequestBody User user);
}
