package com.future.tcfm.service.impl;

import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String PATH = "../assets/";

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public ResponseEntity simpleEmail(EmailRequest emailRequest) {

            // Create a Simple MailMessage.
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(emailRequest.getEmail());
            message.setSubject("Blibli Future Medan Batch - 3.0");
            message.setText("Hello, This is from simple email sender!");

            this.emailSender.send(message);

         return new ResponseEntity<>("Email Sent!", HttpStatus.OK);
        }


    @Override
    public ResponseEntity attachmentEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, emailRequest.getMultipart());

        helper.setTo(emailRequest.getEmail());
        helper.setSubject("Blibli Future Medan Batch - 3.0");
        helper.setText("Hello, This is from attachment email sender!");

        FileSystemResource file = new FileSystemResource(new File(PATH+emailRequest.getFile()));
        helper.addAttachment("Pdf file", file);

        emailSender.send(message);

        return new ResponseEntity<>("Email Sent!", HttpStatus.OK);
    }
}
