package com.future.tcfm.service.impl;

import com.future.tcfm.model.Payment;
import com.future.tcfm.repository.PaymentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void createPayment(){
        List<Payment> payment = new ArrayList<>();
        payment.add(new Payment());
        payment.add(new Payment());
        payment.add(new Payment());
        when(paymentRepository.findAll()).thenReturn(payment);

        ResponseEntity result = paymentService.findAll();
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }
}
