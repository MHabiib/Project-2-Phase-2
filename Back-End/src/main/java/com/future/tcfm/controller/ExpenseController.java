package com.future.tcfm.controller;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.service.ExpenseService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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

//    @GetMapping("/group") //body fill with group name without ""
//    public List<Expense> expenseGroup (@RequestBody String groupName){
//        System.out.println(groupName);
//        return expenseService.expenseGroup(groupName);
//    }

    @GetMapping("/group")
    public List<Expense> expenseGroup (@RequestParam("email") String userEmail){
        return expenseService.expenseGroupByEmail(userEmail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> singleExpense(@PathVariable("id") String id) {
        return expenseService.singleExpense(id);
    }

    @PostMapping
    public ResponseEntity createExpense(@RequestBody Expense expense) {
        return expenseService.createExpense(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") String id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id,expense);
    }

    @PutMapping("/managementExpense")
    public ResponseEntity managementExpense(@RequestBody ExpenseRequest expenseRequest) {
        return expenseService.managementExpense(expenseRequest);
    }
}


