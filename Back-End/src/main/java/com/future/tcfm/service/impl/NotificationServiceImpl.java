package com.future.tcfm.service.impl;

import com.future.tcfm.model.Notification;
import com.future.tcfm.repository.NotificationRepository;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;



@Service
public class NotificationServiceImpl implements NotificationService {

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

    @Override
    public ResponseEntity findAll(){
        List<Notification> notificationList = notificationRepository.findAll();
        return new ResponseEntity<>(notificationList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity findNotificationBy(String email,String groupName, Boolean isRead) {
        List<Notification> notificationList;
        if(isRead==null){
            notificationList=notificationRepository.findByEmailOrGroupName(email,groupName);
            return new ResponseEntity<>(notificationList,HttpStatus.OK);
        }
        notificationList= notificationRepository.findByEmailOrGroupNameAndIsRead(email,groupName,isRead);
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
    public void createNotification(String message,String email, String groupName) {
        Notification notification = Notification.builder()
                .email(email)
                .message(message)
                .isRead(false)
                .timestamp(System.currentTimeMillis())
                .groupName(groupName).build();
        notificationRepository.save(notification);
    }

    @Override
    public ResponseEntity findByEmail(String email) {
        List<Notification> notifications = notificationRepository.findByEmail(email);
        return new ResponseEntity(notifications, HttpStatus.OK);

    }
}
