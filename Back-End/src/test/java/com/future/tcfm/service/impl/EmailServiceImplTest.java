package com.future.tcfm.service.impl;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.service.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailServiceImplTest {
    private EmailServiceImpl emailServiceImpl;

    private JavaMailSender javaMailSender;

    private MimeMessage mimeMessage;

    @Before
    public void before() {
        mimeMessage = new MimeMessage((Session)null);
        javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailServiceImpl = new EmailServiceImpl();
    }

    @Test
    public void emailTest() throws MessagingException {
        String recipient = "example@example.com";
        EmailRequest request = new EmailRequest();
        emailServiceImpl.simpleEmail(request);
        assertEquals(recipient, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }
}
