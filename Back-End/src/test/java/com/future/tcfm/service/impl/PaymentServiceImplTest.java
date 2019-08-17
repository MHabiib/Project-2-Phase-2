package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.*;
import com.future.tcfm.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {
    private static final String USER ="{\"name\":\"Test User\",\"email\":\"userTest@jyp.com\"}";
    private static final String PAYMENT="{\"idPayment\":\"IDDUMMYTEST\",\"groupName\":\"groupTest\",\"email\":\"userTest@jyp.com\"}";
    private static final String ID="IDDUMMYTEST";

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUserDetailsRepository jwtUserDetailsRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private Payment payment;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private NotificationServiceImpl notificationService;


    private User user;
    private Group group;
    private JwtUserDetails jwtUserDetails;
    private MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        user=new User();
        user.setIdUser(ID);
        user.setGroupName("groupTest");
        group=new Group();
        group.setName("groupTest");
        jwtUserDetails=new JwtUserDetails();
        jwtUserDetails.setEmail("userTest@jyp.com");
        jwtUserDetails.setGroupName("groupTest");
    }

    @Test
    public void testCreate() throws IOException, MessagingException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        //to provide security config

        doReturn(user).when(userRepository).findByEmail("userTest@jyp.com");
        doReturn(group).when(groupRepository).findByName(user.getGroupName());
        when(paymentRepository.save(payment)).thenReturn(payment);
        String jsonString = PAYMENT;
        ResponseEntity createdPayment = paymentService.createPayment(jsonString,file);

        when(paymentRepository.count()).thenReturn(1L);

        Assert.assertNotNull("Created payment must not be null", createdPayment);

        Payment payment  = new ObjectMapper().readValue(jsonString, Payment.class);
        Assert.assertEquals(ID, payment.getIdPayment());
        Assert.assertEquals("groupTest", payment.getGroupName());

        verify(paymentRepository, times(1)).save(payment);
        verify(paymentRepository, times(1)).count();
    }

    @Test
    public void testFindAllDataExists() throws IOException, MessagingException {
        when(paymentRepository.save(payment)).thenReturn(payment);
        paymentService.createPayment(PAYMENT,file);
        Payment paymentTest = new Payment();
        paymentTest.setIdPayment(ID);
        when(paymentRepository.save(paymentTest)).thenReturn(paymentTest);
        paymentService.createPayment(PAYMENT,file);

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment, paymentTest));
        ResponseEntity<?> result = paymentService.findAll();
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentRepository, times(1)).save(paymentTest);
        verify(paymentRepository, times(1)).findAll();
    }
}
