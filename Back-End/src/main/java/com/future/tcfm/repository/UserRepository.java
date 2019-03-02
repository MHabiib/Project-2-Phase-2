package com.future.tcfm.repository;

import com.future.tcfm.model.User;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByIdUser(String id);

}

