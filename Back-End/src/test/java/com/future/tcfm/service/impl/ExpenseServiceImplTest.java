package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ExpenseServiceImplTest {
    @Mock
    ExpenseRepository expenseRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ExpenseServiceImpl expenseService;

    private Expense expense;
    private User user;

    @Before
    public void init(){
        expense = new Expense();
        user = new User();
        user.setName("Sana");
        user.setGroupName("BDZ");
        user.setEmail("sana@jyp.com");
        expense.setGroupName("BDZ");
        expense.setTitle("Sound");
        expense.setDetail("Marshall");
        expense.setPrice((double) 15000000);
        expense.setUserContributed(Arrays.asList(user,user,user));
        expense.setRequester(user.getEmail());
    }

    @Test
    public void loadAll() {
        // Data preparation
        List<Expense> expenses = Arrays.asList(expense,expense,expense);
        Mockito.when(expenseRepository.findAll()).thenReturn(expenses);

        // Method call
        List<Expense> expenseList= expenseService.loadAll();

        // Verification
        Assert.assertThat(expenseList, Matchers.hasSize(3));
        Mockito.verify(expenseRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(expenseRepository);
    }

    @Test
    public void expenseGroup() {
        // Data preparation
        List<Expense> expenses= Arrays.asList(expense,expense,expense);
        Mockito.when(expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(expense.getGroupName())).thenReturn(expenses);

        // Method call
        List<Expense> expenseList= expenseService.expenseGroup(expense.getGroupName());

        // Verification
        Assert.assertThat(expenseList, Matchers.hasSize(3));
        Mockito.verify(expenseRepository, Mockito.times(1)).findByGroupNameLikeOrderByCreatedDateDesc(expense.getGroupName());
        Mockito.verifyNoMoreInteractions(expenseRepository);
    }

    @Test
    public void createExpense() {
        doReturn(expense).when(expenseRepository).save(expense);

        ResponseEntity<?> result = expenseService.createExpense(expense);

        Mockito.verify(expenseRepository,Mockito.times(1)).save(expense);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
    }


}
