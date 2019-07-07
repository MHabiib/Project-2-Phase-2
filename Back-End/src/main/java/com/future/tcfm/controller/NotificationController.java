package com.future.tcfm.controller;

import com.future.tcfm.model.Notification;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;


//    @GetMapping(value = "/personal",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity getPersonalNotification(
//            @RequestParam(value = "ref") String email,
//            @RequestParam(value = "isRead",required = false) Boolean isRead) {
//        return notificationService.getPersonalNotification(email, isRead);
//    }
//    @GetMapping(value = "/personal",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Notification> getPersonalNotification(@RequestParam(value = "ref") String email){
//        return notificationService.getPersonalNotificationReactive(email);
//    }

    /**
     * ini yang dipakai
     * @param email
     * @return
     */
    @GetMapping(value = "/personal2",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Notification>> getPersonalNotificationV2(@RequestParam(value = "ref") String email){
        return notificationService.getPersonalNotificationReactiveV2(email);
    }

    @GetMapping(value = "/personal",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPersonalNotification(@RequestParam(value = "ref") String email){
        return notificationService.streamPersonalNotification(email);
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
