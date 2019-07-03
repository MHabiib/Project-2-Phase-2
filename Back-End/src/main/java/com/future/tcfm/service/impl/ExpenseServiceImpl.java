package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
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

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Expense> loadAll() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> expenseGroup(String groupName) {
        return expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(groupName);
    }

    @Override
    public ResponseEntity createExpense(Expense expense) {
        /*Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist != null)
            return new ResponseEntity<>("Failed to request Expense!\nTitle already exists!", HttpStatus.BAD_REQUEST);*/
/*
        if (expense.getGroupName() == null)
            return new ResponseEntity<>("Failed to request Expense!\nGroup not Found!", HttpStatus.BAD_REQUEST);
*/

        expense.setCreatedDate(new Date().getTime());
        expense.setGroupName(userRepository.findByEmail(expense.getRequester()).getGroupName());

        List<User> userContributed = userRepository.findByGroupNameLike(expense.getGroupName());
        expense.setUserContributed(userContributed);
        expense.setRequester(userRepository.findByEmail(expense.getRequester()).getName());
        expenseRepository.save(expense);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @Override
    public ResponseEntity singleExpense(String id) {
        Expense expenseExist = expenseRepository.findByIdExpense(id);
        if (expenseExist!=null)
            return new ResponseEntity<>(expenseExist, HttpStatus.OK);
        else
            return new ResponseEntity<>("Expense Not Found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Expense> expenseGroupByEmail(String userEmail) {
        User userSelected = userRepository.findByEmail(userEmail);
        String userGroup = userSelected.getGroupName();
        return expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(userGroup);
    }

    @Override
    public ResponseEntity updateExpense(String id, Expense expense) {
        Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        Group group= groupRepository.findByName(expense.getGroupName());
        if (expenseExist == null)
            return new ResponseEntity<>("Expense not found!\nerr : 404", HttpStatus.BAD_REQUEST);
        expenseExist.setTitle(expense.getTitle());
        expenseExist.setDetail(expense.getDetail());
        expenseExist.setPrice(expense.getPrice());

        if (expense.getStatus()){
            expense.setApprovedDate(new Date().getTime());
            group.setTotalExpense(group.getTotalExpense()+expense.getPrice());//total expense group
            groupRepository.save(group);
            expenseRepository.save(expense);
        } else if (!expense.getStatus()){
            expense.setRejectedDate(new Date().getTime());
            expenseRepository.save(expense);
        }

        expenseRepository.save(expenseExist);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @Override
    public ResponseEntity managementExpense(ExpenseRequest expenseRequest){
        Expense expenseExist = expenseRepository.findByIdExpense(expenseRequest.getId());
        if (expenseExist==null)
            return new ResponseEntity<>("Expense not found", HttpStatus.OK);
        if(expenseRequest.getStatus()) {
            expenseExist.setStatus(true);
            expenseRepository.save(expenseExist);
        }
        else if(!expenseRequest.getStatus()) {
            expenseExist.setStatus(false);
            expenseRepository.save(expenseExist);
        }
        return new ResponseEntity<>("Expense Updated", HttpStatus.OK);
    }
/*
    @Override
    public ResponseEntity management(ExpenseRequest request) {
        Expense expense = expenseRepository.findByIdExpense(request.getId());
        User user = userRepository.findByEmail(request.getEmail());
        Group groupDtl = groupRepository.findByName(expense.getGroupName());


        if(expense==null)
            return new ResponseEntity<>("Expense Not Found!",HttpStatus.NOT_FOUND);
        if (!expense.getApprovedDate().equals(0L) || !expense.getRejectedDate().equals(0L))
            return new ResponseEntity<>("Expense Already Approved / Rejected!", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Expense Approved",HttpStatus.OK);
    }
*/

}


