package com.future.tcfm.controller;

<<<<<<< HEAD
=======

import com.future.tcfm.model.ReqResModel.ExpenseRequest;
>>>>>>> 60e4bdb6552e6bdf7549935376a457be08ad33df
import com.future.tcfm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
<<<<<<< HEAD
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
=======
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @GetMapping
    public ResponseEntity loadAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{groupName}")
    public ResponseEntity getPaymentByGroupName(@RequestParam("groupName") String groupName) {
        return paymentService.findByGroupName(groupName);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(
            @Nullable @RequestPart("file") MultipartFile file,
            @RequestPart("payment") String paymentJSONString
    ) throws IOException {
        return paymentService.createPayment(paymentJSONString, file);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") String id,
                                 @Nullable @RequestPart("file") MultipartFile file,
                                 @RequestPart("payment") String paymentJSONString) throws IOException {
        return paymentService.updatePayment(id, paymentJSONString, file);
    }

    @PostMapping("/managementPayment")
    public ResponseEntity managementPayment(ExpenseRequest thisPayment){
        return paymentService.managementPayment(thisPayment);
>>>>>>> 60e4bdb6552e6bdf7549935376a457be08ad33df
    }
}
