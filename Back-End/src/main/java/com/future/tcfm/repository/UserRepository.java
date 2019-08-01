package com.future.tcfm.repository;

import com.future.tcfm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByActive(Boolean isActive);
    User findByIdUser(String id);
    User findByEmail(String email);
    List<User> findByGroupNameAndActive(String gName,Boolean bool);
    User findByEmailAndActive(String email,Boolean bool);
    Integer countByGroupName(String groupName);
    Page<User> findByGroupName(String groupName, Pageable pageable);
    List<User> findByGroupNameLike(String groupName);
//    Page<User> findAllByNameContainingAndEmailContainingAndGroupNameContainingAndActiveOrderByTotalPeriodPayed(String name,String email,String groupName,Boolean active,Pageable pageable);
    Page<User> findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndGroupNameContainingIgnoreCaseAndRoleContainingIgnoreCaseAndActiveOrderByTotalPeriodPayed(String name,String email,String groupName,String role,Boolean active,Pageable pageable);

    User findByGroupNameAndRole(String groupName, String role);
    Integer countAllByGroupNameAndActive(String gName, Boolean bool);
    Integer countByGroupNameAndPeriodeTertinggalLessThanAndActive(String gName, Integer pt, Boolean bool);
}

