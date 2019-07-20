package com.future.tcfm.service;

import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface EmailService {
    ResponseEntity simpleEmail(EmailRequest emailRequest);

    void requestExpense(String email, String idExpense) throws MessagingException;

    void userResign(String email) throws MessagingException;

    ResponseEntity attachmentEmail(EmailRequest emailRequest) throws MessagingException;
    void periodicMailSender (String email, Integer range) throws MessagingException;
}
