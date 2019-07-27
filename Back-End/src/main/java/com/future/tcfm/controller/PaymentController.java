package com.future.tcfm.controller;

import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

//    @GetMapping
//    public ResponseEntity loadAll() {
//        return paymentService.findAll();
//    }

//    @GetMapping("/{groupName}")
    @GetMapping
    public ResponseEntity getPaymentByGroupName(
//            @PathVariable("groupName") String groupName,
                                                @RequestParam(value = "filter",required = false, defaultValue = "isPaid")String filter,
                                                @RequestParam(value = "page",required = false, defaultValue = "0")int page,
                                                @RequestParam(value = "size",required = false, defaultValue = "10")int size) {
        return paymentService.findByGroupNameAndIsPaid(getCurrentUser().getGroupName(),filter,page,size);
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

    @PutMapping("/managementPayment")
    public ResponseEntity managementPayment(@RequestBody ExpenseRequest thisPayment) {
        return paymentService.managementPayment(thisPayment);
    }
    @GetMapping("/{id}")
    public ResponseEntity managementPayment(@PathVariable("id") String id) {
        return paymentService.findById(id);
    }
}