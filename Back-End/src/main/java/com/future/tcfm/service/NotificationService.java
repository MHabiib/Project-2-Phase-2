package com.future.tcfm.service;

import com.future.tcfm.model.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    void createNotification(String message,String email,String groupName);
    ResponseEntity findByEmail(String id);
    ResponseEntity findAll();
    ResponseEntity getGroupNotification(String groupName, Boolean bool);
    ResponseEntity updateNotificationIsRead(String id);
    ResponseEntity getPersonalNotification(String email,Boolean isRead);
}
