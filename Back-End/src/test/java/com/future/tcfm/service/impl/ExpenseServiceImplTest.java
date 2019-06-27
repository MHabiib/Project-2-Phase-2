package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.repository.ExpenseRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ExpenseServiceImplTest {
    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ExpenseServiceImpl expenseService;

    private Expense expense;

    @Before
    private void init(){
        expense = new Expense();
        expense.setGroupName("BDZ");
        expense.setTitle("Sound");
        expense.setDetail("Marshall");
        expense.setPrice((double) 15000000);
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
}
