package com.future.tcfm.service.impl;


import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.PaymentRepository;
import com.future.tcfm.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DashboardServiceImplTest {
    @Mock
    ExpenseRepository expenseRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    PaymentRepository paymentRepository;

    @Test
    public void getData() {

    }

}
