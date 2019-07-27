package com.future.tcfm.controller;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.service.ExpenseService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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

//    @GetMapping("/group")
//    public List<Expense> expenseGroup (@RequestParam("email") String userEmail){
//        return expenseService.expenseGroupByEmail(userEmail);
//    }

    /**
     * paging
     * @param userEmail
     * @return
     */
    @GetMapping("/group")
    public Page<Expense> expenseGroup (
            @RequestParam("email") String userEmail,
            @RequestParam(value = "filter",defaultValue = "createdDate") String filter,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size){
        return expenseService.expensePageGroupByEmail(userEmail,filter,page,size);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Expense> singleExpense(@PathVariable("id") String id) {
        return expenseService.singleExpense(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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


