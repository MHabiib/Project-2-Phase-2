package com.future.tcfm.repository;

import com.future.tcfm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByIdUser(String id);
    //List<User> findAllByActive(Boolean bool);
    User findByEmail(String email);
    List<User> findByGroupNameAndActive(String gName,Boolean bool);
    User findByEmailAndActive(String email,Boolean bool);
    Integer countByGroupName(String groupName);
    Page<User> findByGroupName(String groupName, Pageable pageable);
    List<User> findByGroupNameLike(String groupName);
    List<User> findByGroupNameLikeOrderByJoinDateDesc(String groupName);
    User findByGroupNameAndRole(String groupName, String role);
}

