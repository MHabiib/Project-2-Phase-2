package com.future.tcfm.service;

import com.future.tcfm.model.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ExpenseService {
    List<Expense> loadAll();
    ResponseEntity createExpense(Expense expense);
    ResponseEntity updateExpense(@PathVariable("id") String id, @RequestBody Expense expense);
}