package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.model.User;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.ExpenseService;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;
import static com.future.tcfm.service.impl.NotificationServiceImpl.EXPENSE_APPROVED_MESSAGE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.EXPENSE_MESSAGE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.EXPENSE_REJECTED_MESSAGE;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;
    @Override
    public List<Expense> loadAll() {
        return expenseRepository.findAll();
    }

    String notificationMessage;

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
//        expense.setRequester(userRepository.findByEmail(expense.getRequester()).getName());

        expense.setRequester(expense.getRequester());
        expenseRepository.save(expense);
        /*
        Bagian notifikasi...
         */
        String message = expense.getRequester() + EXPENSE_MESSAGE +"(" +expense.getTitle()+")";
        notificationService.createNotification(message,expense.getRequester(),expense.getGroupName());

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


    //ini hanya untuk di akses oleh user utk edit request expense mereka
    @Override
    public ResponseEntity updateExpense(String id, Expense expense) {
        Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist == null)
            return new ResponseEntity<>("Expense not found!\nerr : 404", HttpStatus.BAD_REQUEST);
        expenseExist.setTitle(expense.getTitle());
        expenseExist.setDetail(expense.getDetail());
        expenseExist.setPrice(expense.getPrice());
        expenseExist.setLastModifiedAt(System.currentTimeMillis());
        expenseRepository.save(expenseExist);
        return new ResponseEntity<>(expense, HttpStatus.OK);

    }


    //ini api di pakai untuk admin utk reject / approve request expense dari user group
    @Override
    public ResponseEntity managementExpense(ExpenseRequest expenseRequest){
        Expense expenseExist = expenseRepository.findByIdExpense(expenseRequest.getId());
        if (expenseExist==null)
            return new ResponseEntity<>("Expense not found", HttpStatus.OK);
        if(expenseRequest.getStatus()) {
            expenseExist.setStatus(true);
            //notif...
             notificationMessage = expenseExist.getRequester() + EXPENSE_APPROVED_MESSAGE +"(" +expenseExist.getTitle()+")";
        }
        else if(!expenseRequest.getStatus()) {
            expenseExist.setStatus(false);
            //notif...
            notificationMessage = expenseExist.getRequester() + EXPENSE_APPROVED_MESSAGE +"(" +expenseExist.getTitle()+")";
        }
        notificationService.createNotification(notificationMessage,expenseExist.getRequester(),expenseExist.getGroupName());
        expenseExist.setLastModifiedAt(System.currentTimeMillis());
        expenseExist.setApprovedOrRejectedBy(getCurrentUser().getEmail());
        expenseRepository.save(expenseExist);

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


