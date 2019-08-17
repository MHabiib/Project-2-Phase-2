package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.Payment;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import org.hamcrest.Matchers;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ExpenseServiceImplTest {
    @Mock
    ExpenseRepository expenseRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ExpenseServiceImpl expenseService;

    private Expense expense;
    private User user;
    private Group group;

    @Before
    public void init(){
/*        expense = new Expense();
        user = new User();
        group=new Group();
        user.setName("Sana");
        user.setGroupName("BDZ");
        user.setEmail("sana@jyp.com");
        expense.setGroupName("BDZ");
        expense.setTitle("Sound");
        expense.setDetail("Marshall");
        expense.setPrice((double) 15000000);
        expense.setRequester(user.getEmail());
        group.setName("BDZ");*/
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() throws IOException, MessagingException {
        when(expenseRepository.save(expense)).thenReturn(expense);
        ResponseEntity createdExpense = expenseService.createExpense(expense);
        expense.setTitle("ALBUM");

        when(expenseRepository.count()).thenReturn(1L);

        Assert.assertNotNull("Created category must not be null", createdExpense);
        Assert.assertSame("Created category must equal sample category", expense, createdExpense);
        Assert.assertEquals("Categories must contain only one category", 1L, expenseRepository.count());
        Assert.assertEquals("ALBUM", expense.getTitle());
        Assert.assertEquals("groupTest", expense.getGroupName());

        verify(expenseRepository, times(1)).save(expense);
        verify(expenseRepository, times(1)).count();
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


}
