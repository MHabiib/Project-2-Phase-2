package com.future.tcfm.service;

import com.future.tcfm.model.Payment;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

public interface PaymentService {
    ResponseEntity createPayment(String paymentJSONString, MultipartFile file) throws IOException, MessagingException;
    ResponseEntity updatePayment(String id, String paymentJSONString, MultipartFile file) throws IOException;
    ResponseEntity managementPayment(ExpenseRequest thisPayment) throws MessagingException; // pakai ExpenseRequest karena hanya butuh id dan status
    ResponseEntity findAll();
    ResponseEntity findById(String id);
    ResponseEntity findByEmail(String email, String filter,int page, int size);
    ResponseEntity findByGroupNameAndIsPaid(String groupName,String filter, int page, int size);

    ResponseEntity searchQuery(String key, String value, int page, int size);
}
