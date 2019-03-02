package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setPhone(user.getPhone());
        user.setSuperAdmin(user.getSuperAdmin());
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<User> updateduser(int id, User user) {
        return null;
    }
}
