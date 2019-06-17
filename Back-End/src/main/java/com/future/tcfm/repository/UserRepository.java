package com.future.tcfm.repository;

import com.future.tcfm.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByIdUser(String id);
    //List<User> findAllByActive(Boolean bool);
    User findByEmail(String email);
    Integer countByGroupName(String groupName);
}

