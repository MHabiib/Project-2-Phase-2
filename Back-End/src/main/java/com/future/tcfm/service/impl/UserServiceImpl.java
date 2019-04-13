package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<?> createUser(User user) {
        User userExist = userRepository.findByEmail(user.getEmail());
        if (userExist != null)
            return new ResponseEntity<>("Failed to save User!\nEmail already exists!", HttpStatus.BAD_REQUEST);
        if(user.getEmail()==null)
            return new ResponseEntity<>("Failed to save User!\nGroup can't be null!", HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        user.setBalance(0);//FOR HANDLING NOT NULL PARAMATER
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateUser(String id, User user) {
        User userExist = userRepository.findByIdUser(id);
        if (userExist == null)
            return new ResponseEntity<>("Failed to update User!\nUserId not found!", HttpStatus.NOT_FOUND);
        if(!userExist.getEmail().equals(user.getEmail())) {
            if(userRepository.findByEmail(user.getEmail())!=null)
                return new ResponseEntity<>("Failed to update User!\nEmail already used!", HttpStatus.BAD_REQUEST); ;
        }
        userExist.setEmail(user.getEmail());
        userExist.setName(user.getName());
        userExist.setPhone(user.getPhone());
        userExist.setPassword(user.getPassword());
        userExist.setRole(user.getRole());
        userExist.setBalance(user.getBalance());
        userRepository.save(userExist);
        return new ResponseEntity<>(userExist, HttpStatus.OK);
    }
}

/*    @Override
    public ResponseEntity<?> deleteUser(String id) {
        User userExist = userRepository.findByIdUser(id);
        if (userExist == null)
            return new ResponseEntity<>("Failed to delete User!\nUserId not found!", HttpStatus.BAD_REQUEST);
        userExist.setActive(false);
        userRepository.save(userExist);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }*/
