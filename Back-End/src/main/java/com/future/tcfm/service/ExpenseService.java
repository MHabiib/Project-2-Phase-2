package com.future.tcfm.service;

import com.future.tcfm.model.Expense;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExpenseService {
    List<Expense> loadAll();
    ResponseEntity createExpense(Expense expense);
    ResponseEntity updateExpense(String id,Expense expense);

}