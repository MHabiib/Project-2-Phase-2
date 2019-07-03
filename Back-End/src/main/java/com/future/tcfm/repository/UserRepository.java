package com.future.tcfm.repository;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByIdUser(String id);
    //List<User> findAllByActive(Boolean bool);
    User findByEmail(String email);
    Integer countByGroupName(String groupName);
    List<User> findByGroupNameLike(String groupName);
    List<User> findByGroupNameLikeOrderByJoinDateDesc(String groupName);
}

