package com.future.tcfm.repository;


import com.future.tcfm.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {
    List<Notification> findByUser(String user);
    List<Notification> findByUserAndIsRead(String user, Boolean bool);
}
