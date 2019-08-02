package com.future.tcfm.service.impl;

import com.future.tcfm.model.*;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.model.list.ExpenseIdContributed;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.ExpenseService;
import com.future.tcfm.service.NotificationService;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;
import static com.future.tcfm.service.impl.NotificationServiceImpl.*;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
    public ResponseEntity createExpense(Expense expense) throws MessagingException {
        /*Expense expenseExist = expenseRepository.findByTitle(expense.getTitle());
        if (expenseExist != null)
            return new ResponseEntity<>("Failed to request Expense!\nTitle already exists!", HttpStatus.BAD_REQUEST);*/
        User userExist = userRepository.findByEmail(expense.getRequester());
        Group groupExist = groupRepository.findByName(userExist.getGroupName());
        if (groupExist == null)
            return new ResponseEntity<>("404 :\nGroup not Found!", HttpStatus.NOT_FOUND);
        if(groupExist.getGroupBalance()<expense.getPrice()){
            return new ResponseEntity<>("{\"message\":\"Group balance is not enough\"}",HttpStatus.BAD_REQUEST);
        }
        expense.setCreatedDate(new Date().getTime());
        expense.setGroupName(userRepository.findByEmail(expense.getRequester()).getGroupName());
        expense.setCreatedDate(System.currentTimeMillis());
        List<User> userContributed = userRepository.findByGroupNameLike(expense.getGroupName());
        expense.setUserContributed(userContributed);
//        expense.setRequester(userRepository.findByEmail(expense.getRequester()).getName());

        expense.setRequester(expense.getRequester());
        expenseRepository.save(expense);
        /*
        Bagian notifikasi...
         */
        String message = expense.getRequester() + EXPENSE_MESSAGE +"(" +expense.getTitle()+")";
        notificationService.createNotification(message,expense.getRequester(),expense.getGroupName(),TYPE_GROUP);
        for(User user : userContributed){
            emailService.emailNotification(message,user.getEmail());//pengiriman email untuk user yang berkontribusi pada expense
        }
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

    /**
     * Paging dibawah
     * @param userEmail
     * @return
     */
    @Override
    public Page<Expense> expensePageGroupByEmail(String userEmail,String filter, int page, int size) {
        User userSelected = userRepository.findByEmail(userEmail);
        String userGroup = userSelected.getGroupName();
        return expenseRepository.findByGroupNameOrderByCreatedDateDesc(userGroup,createPageRequest(filter,"desc",page,size));
}

    static Pageable createPageRequest(String filter,String direction,int page, int size) {
        if(direction.equals("asc")){
            return PageRequest.of(page,size,Sort.by(filter).ascending());
        }
        return PageRequest.of(page,size,Sort.by(filter).descending());
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

    @Transactional
    public void updateExpenseContributed(Expense expenseExist,List<User> listUser){
        double balanceUsed = expenseExist.getPrice()/listUser.size();
        List<ExpenseIdContributed> expenseIdContributeds;
        for (User user : listUser) {//add the expense to all user that contributed to this expense
            expenseIdContributeds = user.getExpenseIdContributed();
            if(expenseIdContributeds==null) {
                expenseIdContributeds = new ArrayList<>();
            }
            ExpenseIdContributed e = new ExpenseIdContributed();
            e.setIdExpense(expenseExist.getIdExpense());
            e.setUsedBalance(balanceUsed);
            expenseIdContributeds.add(e);

            user.setBalanceUsed(user.getBalance()+balanceUsed);
            user.setBalance(user.getBalance()-balanceUsed); //mengurangi balance user dengan pembagian pengeluaran
            user.setExpenseIdContributed(expenseIdContributeds);
            userRepository.save(user);
        }
    }
    //ini api di pakai untuk admin utk reject / approve request expense dari user group
    @Override
    public ResponseEntity managementExpense(ExpenseRequest expenseRequest) throws MessagingException {
        Expense expenseExist = expenseRepository.findByIdExpense(expenseRequest.getId());
        if (expenseExist==null)
            return new ResponseEntity<>("Expense not found", HttpStatus.OK);
        if(!expenseExist.getGroupName().equals(getCurrentUser().getGroupName())){
            return new ResponseEntity<>("403 You are not the group admin of this group",HttpStatus.UNAUTHORIZED);
        }
        if(expenseRequest.getStatus()) {
            if(expenseExist.getStatus()!=null){
                return new ResponseEntity<>("Expense already approved!",HttpStatus.BAD_REQUEST);
            }
            expenseExist.setStatus(true);
            //notif...
            Group group = groupRepository.findByName(expenseExist.getGroupName());
            group.setGroupBalance(group.getGroupBalance()-expenseExist.getPrice());
            List<User> listUser = userRepository.findByGroupNameLike(group.getName());
            group.setBalanceUsed(group.getBalanceUsed()+expenseExist.getPrice());
            updateExpenseContributed(expenseExist,listUser);//update the user field with transactional

            groupRepository.save(group);
            notificationMessage = expenseExist.getRequester() + EXPENSE_APPROVED_MESSAGE +"(" +expenseExist.getTitle()+")";
        }
        else if(!expenseRequest.getStatus()) {
            expenseExist.setStatus(false);
            //notif...
            notificationMessage = expenseExist.getRequester() + EXPENSE_REJECTED_MESSAGE +"(" +expenseExist.getTitle()+")";
        }
        notificationService.createNotification(notificationMessage,expenseExist.getRequester(),expenseExist.getGroupName(),TYPE_GROUP);
        List<User> groupMembers = userRepository.findByGroupNameLike(expenseExist.getGroupName());
        for(User user : groupMembers){
            emailService.emailNotification(notificationMessage,user.getEmail());//pengiriman email untuk user yang berkontribusi
        }
        expenseExist.setLastModifiedAt(System.currentTimeMillis());
        expenseExist.setApprovedOrRejectedBy(getCurrentUser().getEmail());
        expenseRepository.save(expenseExist);
        return new ResponseEntity<>("Expense Updated", HttpStatus.OK);
    }

    @Override
    public Page<Expense> searchBy(String query, int page, int size) throws ParseException {
        System.out.println("Query Param : "+query);
        Pattern pattern = Pattern.compile("(.*)(:|<|>)(.*)");
        Matcher matcher = pattern.matcher(query);
        if(!matcher.find()){return null;}
        String key=matcher.group(1);
        String value=matcher.group(3);
        System.out.println("Key : "+key+"; Value : "+value);
        if(key.equalsIgnoreCase("title")){
            return expenseRepository.findByTitleContainsIgnoreCaseOrderByCreatedDateDesc(value,createPageRequest("createdDate","desc",page,size));
        } else if(key.equalsIgnoreCase("status")){
            Boolean status = null;
            status = value.equalsIgnoreCase("accepted");
            return expenseRepository.findByStatusOrderByCreatedDateDesc(status,createPageRequest("createdDate","desc",page,size));
        } else if(key.equalsIgnoreCase("price lt" )){
            Double dValue = value.equalsIgnoreCase("")? Double.MAX_VALUE : Double.parseDouble(value);
            return expenseRepository.findByPriceLessThanEqualOrderByCreatedDate(dValue,createPageRequest("lastModifiedAt","desc",page,size));
        } else if(key.equalsIgnoreCase("price gt" )) {
            Double dValue = value.equalsIgnoreCase("")? 0 :Double.parseDouble(value);
            return expenseRepository.findByPriceGreaterThanEqualOrderByCreatedDate(dValue, createPageRequest("lastModifiedAt", "desc", page, size));
        } else if(key.equalsIgnoreCase("date before")){
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            long timeStamp= System.currentTimeMillis();
            try {
                timeStamp = formatter.parse(value).getTime();
            }catch (Exception e){e.printStackTrace();}
            return expenseRepository.findByCreatedDateLessThanEqualOrderByStatus(timeStamp,createPageRequest("createdDate","desc",page,size));
        } else if(key.equalsIgnoreCase("date after")){
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            long timeStamp= 0;
            try {
                timeStamp = formatter.parse(value).getTime();
            }catch (Exception e){e.printStackTrace();}
            return expenseRepository.findByCreatedDateGreaterThanEqualOrderByStatus(timeStamp,createPageRequest("createdDate","desc",page,size));
        }

        return null;
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


