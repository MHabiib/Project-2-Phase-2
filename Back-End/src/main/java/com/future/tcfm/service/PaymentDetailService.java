package com.future.tcfm.service;

import org.springframework.http.ResponseEntity;

public interface PaymentDetailService {
    ResponseEntity findAll();
    ResponseEntity findById();
}
