package com.future.tcfm.repository;

import com.future.tcfm.model.JwtUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JwtUserDetailsRepository extends MongoRepository<JwtUserDetails, String> {
    JwtUserDetails findByEmail(String email);
    JwtUserDetails findByTokenAndRefreshToken(String token,String refreshToken);
}
