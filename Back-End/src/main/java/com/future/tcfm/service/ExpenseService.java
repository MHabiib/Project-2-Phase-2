package com.future.tcfm.service;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.util.List;

public interface ExpenseService {
    List<Expense> loadAll();
    List<Expense> expenseGroup(String groupName);
    ResponseEntity createExpense(Expense expense) throws MessagingException;
    ResponseEntity singleExpense(String id);
    Page<Expense> expensePageGroupByEmail(String userEmail,String filter, int page, int size);
    ResponseEntity updateExpense(String id, Expense expense);
    ResponseEntity managementExpense(ExpenseRequest expenseRequest) throws MessagingException;
    //ResponseEntity management(ExpenseRequest request);
    List<Expense> expenseGroupByEmail(String userEmail);
}