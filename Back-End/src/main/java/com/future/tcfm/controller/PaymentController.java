package com.future.tcfm.controller;

import com.future.tcfm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController("/api/payment")
public class PaymentController {
    private final
    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity createPayment(
            @RequestPart("paymentModel") String paymentModel,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return paymentService.createPayment(paymentModel, file);
    }
}
