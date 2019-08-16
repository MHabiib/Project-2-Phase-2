package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.Payment;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.PaymentRepository;
import com.future.tcfm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {
    private static final String USER ="{\"name\":\"Test User\",\"email\":\"userTest@jyp.com\"}";
    private static final String PAYMENT="{\"idPayment\":\"testID\",\"groupName\":\"groupTest\",\"email\":\"userTest@jyp.com\"}";

    @Mock
    private UserRepository userRepository;

    private Payment payment;
    private PaymentRepository paymentRepository;
    private PaymentServiceImpl paymentService;
    private User user;
    private UserServiceImpl userService;
    MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    @Before
    public void setUp() throws Exception {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentServiceImpl(paymentRepository);
        userService=new UserServiceImpl(userRepository);
        userRepository=mock(UserRepository.class);
    }

    @Test
    public void testCreate() throws IOException, MessagingException {
        when(paymentRepository.save(payment)).thenReturn(payment);
        String jsonString = PAYMENT;
        ResponseEntity createdPayment = paymentService.createPayment(jsonString,file);

        when(paymentRepository.count()).thenReturn(1L);

        Assert.assertNotNull("Created category must not be null", createdPayment);
        Assert.assertSame("Created category must equal sample category", payment, createdPayment);
        Assert.assertEquals("Categories must contain only one category", 1L, paymentRepository.count());
        Payment payment  = new ObjectMapper().readValue(jsonString, Payment.class);
        Assert.assertEquals("testID", payment.getIdPayment());
        Assert.assertEquals("groupTest", payment.getGroupName());

        verify(paymentRepository, times(1)).save(payment);
        verify(paymentRepository, times(1)).count();
    }

    @Test
    public void testFindAllDataExists() {
/*        when(categoryRepository.save(category)).thenReturn(category);
        categoryService.create(category);
        Category secondCategory = new Category("DISH", "Dish");
        when(categoryRepository.save(secondCategory)).thenReturn(secondCategory);
        categoryService.create(secondCategory);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, secondCategory));
        List<Category> foundCategories = categoryService.findAll();

        Assert.assertNotNull("Found categories must not be null", foundCategories);
        Assert.assertEquals("Categories must contain only two categories", 2, foundCategories.size());
        Assert.assertSame("Found category must equal sample category (BOTTLE)", category, foundCategories.get(0));
        Assert.assertSame("Found category must equal sample category (DISH)", secondCategory, foundCategories.get(1));
        Assert.assertEquals("BOTTLE", foundCategories.get(0).getId());
        Assert.assertEquals("Bottle", foundCategories.get(0).getName());
        Assert.assertEquals("DISH", foundCategories.get(1).getId());
        Assert.assertEquals("Dish", foundCategories.get(1).getName());

        verify(categoryRepository, times(1)).save(category);
        verify(categoryRepository, times(1)).save(secondCategory);
        verify(categoryRepository, times(1)).findAll();*/
    }
}
