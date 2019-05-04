package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist != null)
            return new ResponseEntity<>("Failed to request Expense!\nEmail already exists!", HttpStatus.BAD_REQUEST);
        expense.setCreatedDate(new Date().getTime());
        expenseRepository.save(expense);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateExpense(String id, Expense expense) {
        Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist == null)
            return new ResponseEntity<>("Expense not found!\nerr : 404", HttpStatus.BAD_REQUEST);
        expenseExist.setTitle(expense.getTitle());
        expenseExist.setDetail(expense.getDetail());
        expenseExist.setPrice(expense.getPrice());

        if (expense.getStatus()==true){
            expense.setApprovedDate(new Date().getTime());
            expense.setApproverList(expense.getApproverList());
            expense.setContributorList(expense.getContributorList());
        } else if (expense.getStatus()==false){
            expense.setRejectedDate(new Date().getTime());
            expense.setApproverList(expense.getApproverList());
        }

        expenseRepository.save(expenseExist);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }
}


