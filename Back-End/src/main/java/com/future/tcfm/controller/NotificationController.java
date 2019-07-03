package com.future.tcfm.controller;

import com.future.tcfm.model.Notification;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    NotificationService notificationService;


    @GetMapping(value = "/notification",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getNotification(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "groupName", required = false,defaultValue = "-1") String groupName,
            @RequestParam(value = "isRead",required = false) Boolean isRead) {
        return notificationService.findNotificationBy(email,groupName, isRead);
    }

    @PutMapping(value = "/notification/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateNotification(
            @PathVariable("id") String id){
        return notificationService.updateNotificationIsRead(id);
    }
}
