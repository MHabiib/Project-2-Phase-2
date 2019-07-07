package com.future.tcfm.service;

import com.future.tcfm.model.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

public interface NotificationService {
    void createNotification(String message,String email,String groupName);
    ResponseEntity findByEmail(String id);
    ResponseEntity findAll();
    ResponseEntity getGroupNotification(String groupName, Boolean bool);
    ResponseEntity updateNotificationIsRead(String id);
    ResponseEntity getPersonalNotification(String email,Boolean isRead);

    Flux<Notification> getPersonalNotificationReactive(String email);

    //    Flux<Notification> getPersonalNotificationReactive();
    Flux<List<Notification>> getPersonalNotificationReactiveV2(String email);

    SseEmitter streamPersonalNotification(String email);
}
