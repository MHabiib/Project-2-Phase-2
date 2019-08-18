package com.future.tcfm.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.*;
import com.future.tcfm.repository.*;
import com.future.tcfm.service.ExpenseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DashboardServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private Group group;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private Expense expense;
    private Expense expense2;
    private User user;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        user=new User();
        group=new Group();
        expense = new Expense();
        expense2 = new Expense();

        user.setEmail("userTest@jyp.com");
        user.setGroupName("groupTest");
        User user2 = new User();
        user2.setEmail("userTest2@jyp.com");
        user2.setGroupName("groupTest");
        expense.setTitle("TestExpense");
        expense2.setTitle("TestExpense2");
    }

    @Test
    public void testDashboardData(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(groupRepository.findByNameAndActive(user.getGroupName(),true)).thenReturn(group);
        when(userRepository.countByGroupName(user.getGroupName())).thenReturn(2);
        when(expenseRepository.findByGroupNameLikeAndStatusOrderByCreatedDateDesc(user.getGroupName(),true)).thenReturn(Arrays.asList(expense, expense2));

        Dashboard dashboard=dashboardService.getData(user.getEmail());

        User foundUser = userRepository.findByEmail(user.getEmail());
        Group foundGroup = groupRepository.findByNameAndActive(user.getGroupName(),true);
        int countGroup = userRepository.countByGroupName(user.getGroupName());
//        List<Expense> foundExpenses = expenseRepository.findByGroupNameLikeAndStatusOrderByCreatedDateDesc(user.getGroupName(),true);

        Assert.assertNotNull("Found user must not be null", dashboard.getAdminAccountNumber());
        Assert.assertNotNull("Found group must not be null", dashboard.getAdminName());
        Assert.assertNotNull("Count group must not be null", dashboard.getExpenseByQuantitiyPercent());
      /*  Assert.assertNotNull("Found expenses must not be null", foundExpenses);
        Assert.assertEquals("Expense must contain only two expenses", 2, foundExpenses.size());
        Assert.assertSame("Found expense must equal sample category TestExpense", expense, foundExpenses.get(0));
        Assert.assertSame("Found expense must equal sample category TestExpense2", expense2, foundExpenses.get(1));
        Assert.assertEquals("TestExpense", foundExpenses.get(0).getTitle());
        Assert.assertEquals("TestExpense2", foundExpenses.get(1).getTitle());
*/
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
}
