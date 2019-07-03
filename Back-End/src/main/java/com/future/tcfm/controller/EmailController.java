package com.future.tcfm.controller;

import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RequestMapping("/api/email")
@Controller
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/simpleEmail")
    public ResponseEntity simpleEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.simpleEmail(emailRequest);
    }
    @PostMapping("/attachmentEmail")
    public ResponseEntity attachmentEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        return emailService.attachmentEmail(emailRequest);
    }
}
