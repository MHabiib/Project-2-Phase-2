package com.future.tcfm.service;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.request.ExpenseRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExpenseService {
    List<Expense> loadAll();
    List<Expense> expenseGroup(String groupName);
    ResponseEntity createExpense(Expense expense);
    ResponseEntity updateExpense(String id,Expense expense);
    ResponseEntity management(ExpenseRequest request);
    List<Expense> expenseGroupByEmail(String userEmail);
}