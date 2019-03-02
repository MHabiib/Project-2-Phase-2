package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    public List<Expense> loadAll() {
        return expenseRepository.findAll();
    }

    @Override
    public ResponseEntity createExpense(Expense expense) {
        return null;
    }

    @Override
    public ResponseEntity updateExpense(String id, Expense expense) {
        return null;
    }
}
