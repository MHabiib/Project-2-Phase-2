package com.future.tcfm.service.impl;

import com.future.tcfm.model.NotificationEvent;
import com.future.tcfm.model.Notification;
import com.future.tcfm.repository.NotificationRepository;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;


@Service
@EnableScheduling
public class NotificationServiceImpl implements NotificationService {
    public static final String TYPE_PERSONAL = "PERSONAL";
    public static final String TYPE_GROUP = "GROUP";
    public static final String GROUP_PROFILE_UPDATE = "GROUP";
    public static final String PAYMENT_DUE_DATE = " please make your payment now ";
    public static final String EXPENSE_MESSAGE = " requested new expense ";
    public static final String EXPENSE_APPROVED_MESSAGE = " 's requested expense had been approved ";
    public static final String EXPENSE_REJECTED_MESSAGE = " 's requested expense had been rejected ";
    public static final String USER_LEFT_GROUP = " just left this group ";
    public static final String USER_JOINED_GROUP = " just joined this group ";
    public static final String PAYMENT_MESSAGE = " had made payment ";
    public static final String PAYMENT_APPROVED_MESSAGE = " 's payment had been approved/confirmed by ";
    public static final String PAYMENT_REJECTED_MESSAGE = " 's payment had been rejected by ";


    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @Override
    public ResponseEntity findAll(){
        List<Notification> notificationList = notificationRepository.findAll();
        return new ResponseEntity<>(notificationList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity getGroupNotification(String groupName, Boolean isRead) {
        List<Notification> notificationList;
        if(isRead==null){
            notificationList=notificationRepository.findByGroupNameOrderByTimestampDesc(groupName);
            return new ResponseEntity<>(notificationList,HttpStatus.OK);
        }
        notificationList= notificationRepository.findByGroupNameAndIsReadOrderByTimestampDesc(groupName,isRead);
        return new ResponseEntity<>(notificationList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity getPersonalNotification(String email,Boolean isRead) {
        List<Notification> notificationList;
        if(isRead==null){
            notificationList=notificationRepository.findByEmailOrderByTimestampDesc(email);
            return new ResponseEntity<>(notificationList,HttpStatus.OK);
        }
        notificationList= notificationRepository.findByEmailAndIsReadOrderByTimestampDesc(email,isRead);
        return new ResponseEntity<>(notificationList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateNotificationIsRead(String id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if(notification.isPresent()) {
            notification.get().setIsRead(true);
            notification.get().setIsReadAt(System.currentTimeMillis());
            notificationRepository.save(notification.get());
            return new ResponseEntity<>(notification.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>("err: Notification Not found 404",HttpStatus.NOT_FOUND);
    }

    @Override
    public void createNotification(String message, String email, String groupName, String type) {
        Notification notification = Notification.builder()
                .email(email)
                .message(message)
                .isRead(false)
                .timestamp(System.currentTimeMillis())
                .groupName(groupName)
                .type(type).build();
        notificationRepository.save(notification);
        NotificationEvent notificationEvent = new NotificationEvent(this,type,email,groupName);
        applicationEventPublisher.publishEvent(notificationEvent);
    }

    @Override
    public ResponseEntity findByEmail(String email) {
        List<Notification> notifications = notificationRepository.findByEmailOrderByTimestampDesc(email);
        return new ResponseEntity(notifications, HttpStatus.OK);
    }
    String email;
    private List<Notification> getNotificationByEmail(long interval){

        return notificationRepository.findByEmailOrderByTimestampDesc(email);
//        return notificationRepository.findAll();
    }

    /**
     *
     * @return notification one by one every X seconds
     */
    @Override
    public Flux<Notification> getPersonalNotificationReactive(String email){
//        this.email = getCurrentUser().getEmail();
        this.email = email;
        return Flux.interval(Duration.ofSeconds(1))
                .onBackpressureDrop()
                .map(this::getNotificationByEmail)
                .flatMapIterable(x -> x);
    }
    /**
     *
     * @Return notificationList every X secodns
     */
    List<Notification> notificationList=new ArrayList<>();
    @Override
    public Flux<List<Notification>> getPersonalNotificationReactiveV2(String email){
//        this.email = getCurrentUser().getEmail();
        this.notificationList = notificationRepository.findTop10ByEmailOrderByTimestampDesc(email);//problem disini
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<List<Notification>> notificationFlux = Flux.fromStream(Stream.generate(() -> this.notificationList));
        return Flux.zip(interval, notificationFlux).map(Tuple2::getT2);
    }

//    /**
//     * Stream Notifikasi yang digunakan adalah yang dibawah ini.
//     * Hanya mengirim event ketika ada perubahan pada database
//     * @param email
//     * @return
//     */
//    @Override
//    public SseEmitter streamPersonalNotification(String email) {
//        SseEmitter emitter = new SseEmitter();
//
//        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
//        this.notificationList=notificationRepository.findTop10ByEmailOrderByTimestampDesc(email);
//        sseMvcExecutor.execute(() -> {
//        SseEmitter.SseEventBuilder event = SseEmitter.event();
//            try {
//                event.id(UUID.randomUUID().toString());
//                event.name("start");
//                event.data(this.notificationList);
//                emitter.send(event);
//                System.out.println("first notification is sent");
//                for (int i = 0; true; i++) {
//                    if(this.notificationList.size()!=notificationRepository.findTop10ByEmailOrderByTimestampDesc(email).size()) {
//                        this.notificationList = notificationRepository.findTop10ByEmailOrderByTimestampDesc(email);
//                        event = SseEmitter.event()
//                                .id(String.valueOf(i+"_"+UUID.randomUUID().toString()))
//                                .name("message")
//                                .data(this.notificationList);
//                        emitter.send(event);
//                        System.out.println("new update on notification is sent");
//                    }
//                    Thread.sleep(1000);
//                }
//            } catch (Exception ex) {
//                emitter.completeWithError(ex);
//            }
//        });
//
//        return emitter;
//    }

    @Override
    public SseEmitter streamNotification(String ref,String type) {
        SseEmitter emitter = new SseEmitter(60*1000L);
        List<Notification> notificationList;
        if(type.equalsIgnoreCase(TYPE_GROUP)) {
           notificationList = notificationRepository.findTop10ByGroupNameOrderByTimestampDesc(ref);
        }
        else{
            notificationList = notificationRepository.findTop10ByEmailOrderByTimestampDesc(ref);
        }
        this.emitters.add(emitter);
        try{
            SseEmitter.SseEventBuilder event = SseEmitter.event();
            event.data(notificationList);
            event.id(UUID.randomUUID().toString());
            event.name("start");
            emitter.send(event);
        }catch (Exception ex){
            this.emitters.remove(emitter);
        }
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(emitter);
        });
        return emitter;
    }

    @Async
    @EventListener
    public void onNewNotification(NotificationEvent notificationEvent){
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        List<SseEmitter> deadEmitters = new ArrayList<>();
        List<Notification> notificationList;
        if (notificationEvent.getType().equalsIgnoreCase(TYPE_GROUP)) {
            notificationList = notificationRepository.findTop10ByGroupNameOrderByTimestampDesc(notificationEvent.getGroupName());
            System.out.println("Event Group triggered!");
        } else {
            notificationList = notificationRepository.findTop10ByEmailOrderByTimestampDesc(notificationEvent.getEmail());
            System.out.println("Event Personal triggered!");
        }
        sseMvcExecutor.execute(() -> {
            SseEmitter.SseEventBuilder event = SseEmitter.event();
            this.emitters.forEach(emitter -> {
                try {
                    event.name("message");
                    event.id(UUID.randomUUID().toString());
                    event.data(notificationList);
                    emitter.send(event);
                } catch (Exception e) {
                    deadEmitters.add(emitter);
                }
            });
            this.emitters.remove(deadEmitters);
            });
    }
}
