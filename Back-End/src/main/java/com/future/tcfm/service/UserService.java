package com.future.tcfm.service;

import com.future.tcfm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> loadAll();
    ResponseEntity getUserById(String id);
    User getUser(String email);
    ResponseEntity createUserV2(String userJSONString, MultipartFile file) throws IOException;

//    ResponseEntity<?> createUser(User user);

    ResponseEntity manageUser(String id, User user);

    ResponseEntity updateUserV2(String id, String userJSONString, MultipartFile file) throws IOException;
    ResponseEntity getImage(String imageName) throws IOException;

//    ResponseEntity<?> updateUser(String id, User user);
    Page<User> searchByNameAndGroupName(String name, String groupName,String filter, int page, int size);

    ResponseEntity deleteUser(String email);
}
