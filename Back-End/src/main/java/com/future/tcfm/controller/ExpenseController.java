package com.future.tcfm.controller;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.request.ExpenseRequest;
import com.future.tcfm.service.ExpenseService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@NoArgsConstructor
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping
    public List<Expense> loadAll (){
        return expenseService.loadAll();
    }

    @GetMapping("/group") //body fill with group name without ""
    public List<Expense> expenseGroup (@RequestBody String groupName){
        return expenseService.expenseGroup(groupName);
    }

    @PostMapping
    public ResponseEntity createExpense(@RequestBody Expense expense) {
        return expenseService.createExpense(expense);
    }

    @PostMapping("/management")
    public ResponseEntity management(@RequestBody ExpenseRequest request){return expenseService.management(request);}

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") String id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id,expense);
    }
}


