package com.future.tcfm.controller;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Expense;
import com.future.tcfm.service.ExpenseService;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/expense")
    public List<Expense> loadAll (){
        return expenseService.loadAll();
    }

    @PostMapping("/expense")
    public ResponseEntity createExpense(@RequestBody Expense expense) {
        return expenseService.createExpense(expense);
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") String id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id,expense);
    }
}


