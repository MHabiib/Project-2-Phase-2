package com.future.tcfm.service;

import com.future.tcfm.model.Payment;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PaymentService {
    ResponseEntity createPayment(String paymentJSONString, MultipartFile file) throws IOException;
    ResponseEntity updatePayment(String id, String paymentJSONString, MultipartFile file) throws IOException;
    ResponseEntity managementPayment(ExpenseRequest thisPayment); // pakai ExpenseRequest karena hanya butuh id dan status
    ResponseEntity findAll();
    ResponseEntity findById();
    ResponseEntity findByIdUser();
    ResponseEntity findByGroupName(String groupName);
}
