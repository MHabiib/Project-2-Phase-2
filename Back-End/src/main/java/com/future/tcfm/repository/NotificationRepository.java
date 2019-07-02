package com.future.tcfm.repository;


import com.future.tcfm.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {
    List<Notification> findByEmail(String email);
    List<Notification> findByEmailOrGroupNameAndIsRead(String email,String GroupName, Boolean bool);
    List<Notification> findByEmailOrGroupName(String email,String groupName);


}
