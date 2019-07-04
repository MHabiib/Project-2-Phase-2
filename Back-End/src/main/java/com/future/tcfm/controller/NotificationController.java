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
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;


    @GetMapping(value = "/personal",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPersonalNotification(
            @RequestParam(value = "ref") String email,
            @RequestParam(value = "isRead",required = false) Boolean isRead) {
        return notificationService.getPersonalNotification(email, isRead);
    }
    @GetMapping(value = "/group",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGroupNotification(
            @RequestParam(value = "ref") String email,
            @RequestParam(value = "isRead",required = false) Boolean isRead) {
        return notificationService.getGroupNotification(email, isRead);
    }

    @PutMapping(value = "/notification/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateNotification(
            @PathVariable("id") String id){
        return notificationService.updateNotificationIsRead(id);
    }
}
