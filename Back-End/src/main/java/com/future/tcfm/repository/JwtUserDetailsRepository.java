package com.future.tcfm.repository;

import com.future.tcfm.model.JwtUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JwtUserDetailsRepository extends MongoRepository<JwtUserDetails, String> {
    //List<User> findAllByActive(Boolean bool);
    JwtUserDetails findByEmail(String email);
}
