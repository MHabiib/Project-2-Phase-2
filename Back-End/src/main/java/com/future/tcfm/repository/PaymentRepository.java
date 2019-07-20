package com.future.tcfm.repository;

import com.future.tcfm.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment,String> {
    Payment findByIdPayment(String id);
    List<Payment> findAll();
    Page<Payment> findAllByGroupNameOrderByLastModifiedAt(String groupName,Pageable pageable);
    Page<Payment> findAllByEmailOrderByLastModifiedAt(String email, Pageable pageable);
    List<Payment>findByEmailAndIsPaid(String email, Boolean isPaid);
}
