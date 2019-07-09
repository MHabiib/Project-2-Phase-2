package com.future.tcfm.controller;

import com.future.tcfm.model.User;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity loadAll() {
        return ResponseEntity.ok(userService.loadAll());
    }

    @GetMapping("/email") // ini seharusnya gk usah, cukup @GetMapping aja gmn? biar jadi /api/user?email=value
    public User getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(
            @Nullable @RequestPart("file") MultipartFile file,
            @RequestPart("user") String userJSONString
    ) throws IOException {
        return userService.createUserV2(userJSONString, file);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") String id,
                                 @Nullable @RequestPart("file") MultipartFile file,
                                 @RequestPart("user") String userJSONString) throws IOException {
        return userService.updateUserV2(id, userJSONString, file);
    }
}

//    @PostMapping
//    public ResponseEntity createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }

//    @DeleteMapping(value = "/user/{id}",produces = MediaType.APPLICATION_JSON_VALUE  )
//    public ResponseEntity delete(@PathVariable("id")int id){
//        return userService.deleteUser(id);
//    }
//}
//    @PutMapping("/{id}")
//    public ResponseEntity updateUser(@PathVariable("id") String id, @RequestBody User user) {
//        return userService.updateUser(id,user);
//    }