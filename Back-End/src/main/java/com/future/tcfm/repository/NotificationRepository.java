package com.future.tcfm.repository;


import com.future.tcfm.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {
    List<Notification> findByEmailOrderByTimestampDesc(String email);
    List<Notification> findByGroupNameOrderByTimestampDesc(String email);
    List<Notification> findByEmailOrGroupNameAndIsRead(String email,String GroupName, Boolean bool);
    List<Notification> findByEmailOrGroupName(String email,String groupName);
    List<Notification> findByEmailAndIsReadOrderByTimestampDesc(String email,Boolean bool);
    List<Notification> findByGroupNameAndIsReadOrderByTimestampDesc(String email,Boolean bool);
}
