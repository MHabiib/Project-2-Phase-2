package com.future.tcfm.controller;

import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/img")
public class ImageController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/{imageName:.+}")
    public ResponseEntity getImage(@PathVariable("imageName") String imageName) throws IOException {
        return userService.getImage(imageName);
    }
}
