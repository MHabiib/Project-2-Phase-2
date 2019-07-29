package com.future.tcfm.service;

import com.future.tcfm.model.ReqResModel.EmailRequest;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface EmailService {
    ResponseEntity simpleEmail(EmailRequest emailRequest);

    void emailNotification(String message, String email)  throws MessagingException;

    ResponseEntity userResign(String email) throws MessagingException;

    ResponseEntity attachmentEmail(EmailRequest emailRequest) throws MessagingException;
    void periodicMailSender (String email, String monthNowStr, String monthBeforeStr) throws MessagingException;
}
