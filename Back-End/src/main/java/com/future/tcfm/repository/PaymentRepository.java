package com.future.tcfm.repository;

import com.future.tcfm.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment,String> {
    Payment findByIdPayment(String id);
    List<Payment> findAll();
    List<Payment> findAllByGroupNameOrderByLastModifiedAt(String groupName);
    List<Payment> findAllByEmailOrderByLastModifiedAt(String id);
}
