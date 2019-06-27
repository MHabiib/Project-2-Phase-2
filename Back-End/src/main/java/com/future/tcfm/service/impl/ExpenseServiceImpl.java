package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.model.list.ApproverContributor;
import com.future.tcfm.model.request.ExpenseRequest;
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
    public ResponseEntity createExpense(Expense expense) {
        Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist != null)
            return new ResponseEntity<>("Failed to request Expense!\nTitle already exists!", HttpStatus.BAD_REQUEST);

        expense.setCreatedDate(new Date().getTime());
        expense.setGroupName(expense.getGroupName());
        if (expense.getGroupName() == null)
            return new ResponseEntity<>("Failed to request Expense!\nGroup not Found!", HttpStatus.BAD_REQUEST);

        List<User> userContributed = userRepository.findByGroupNameLike(expense.getGroupName());
        expense.setUserContributed(userContributed);
        expense.setRequester(userRepository.findByEmail(expense.getRequester()).getName());
        expenseRepository.save(expense);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @Override
    public List<Expense> expenseGroup(String groupName) {
        return expenseRepository.findByGroupNameLike(groupName);
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
            expense.setApproverList(expense.getApproverList());
            expense.setContributorList(expense.getContributorList());
            group.setTotalExpense(group.getTotalExpense()+expense.getPrice());//total expense group
            groupRepository.save(group);
            expenseRepository.save(expense);
        } else if (!expense.getStatus()){
            expense.setRejectedDate(new Date().getTime());
            expense.setApproverList(expense.getApproverList());
            expenseRepository.save(expense);
        }

        expenseRepository.save(expenseExist);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @Override
    public ResponseEntity management(ExpenseRequest request) {
        Expense expense = expenseRepository.findByIdExpense(request.getId());
        User user = userRepository.findByEmail(request.getEmail());
        Group groupDtl = groupRepository.findByName(expense.getGroupName());
        List<ApproverContributor> contributorList = expense.getContributorList();
        List<ApproverContributor> approverList = expense.getContributorList();

        if(expense==null)
            return new ResponseEntity<>("Expense Not Found!",HttpStatus.NOT_FOUND);
        if (!expense.getApprovedDate().equals(0L) || !expense.getRejectedDate().equals(0L))
            return new ResponseEntity<>("Expense Already Approved / Rejected!", HttpStatus.BAD_REQUEST);

        if(!request.getApproveStatus()){
                ApproverContributor a = new ApproverContributor();
                a.setEmail(request.getEmail());
                a.setName(user.getName());
                contributorList.add(a);
                expenseRepository.save(expense);
                return new ResponseEntity<>("Expense Rejected!", HttpStatus.OK);
        }
        ApproverContributor b = new ApproverContributor();
        b.setEmail(request.getEmail());
        b.setName(user.getName());
        approverList.add(b);
        contributorList.add(b);
        expense.setPercentageApproved(expense.getPercentageApproved()+(1/(userRepository.findByGroupNameLike(expense.getGroupName()).size())));
        expenseRepository.save(expense);


        if(expense.getPercentageApproved()>=25){
            expense.setStatus(true);
            groupDtl.setTotalExpense(groupDtl.getTotalExpense()+expense.getPrice());
            expenseRepository.save(expense);
            groupRepository.save(groupDtl);
            return new ResponseEntity<>("Expense Approved and Got More Than 2Approved!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Expense Approved",HttpStatus.OK);
    }


    @Override
    public List<Expense> expenseGroupByEmail(String userEmail) {
        User userSelected = userRepository.findByEmail(userEmail);
        String userGroup = userSelected.getGroupName();
        return expenseRepository.findByGroupNameLike(userGroup);
    }
}


