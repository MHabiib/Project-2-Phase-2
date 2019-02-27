package com.future.tcfm.repository;

import com.future.tcfm.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}

