package com.future.tcfm.service.impl;

import com.future.tcfm.model.*;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.PaymentRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final
    GroupRepository groupRepository;

    private final
    ExpenseRepository expenseRepository;

    private final
    UserRepository userRepository;

    private final
    PaymentRepository paymentRepository;

    @Autowired
    public DashboardServiceImpl(GroupRepository groupRepository, UserRepository userRepository, ExpenseRepository expenseRepository, PaymentRepository paymentRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Dashboard getData(String email) {
        User dUser = userRepository.findByEmail(email);
        Group dGroup = groupRepository.findByName(dUser.getGroupName());
        Integer totalMembers = userRepository.countByGroupName(dGroup.getName());
        List<Expense> dExpense = expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(dUser.getGroupName());

        User groupAdmin = userRepository.findByGroupNameAndRole(dUser.getGroupName(), "GROUP_ADMIN");
        String adminName = groupAdmin.getName();
        String accountNumber = groupAdmin.getRekening();

        int monthNow= LocalDate.now().getMonthValue();
        double expenseByValue = 0;
        int expenseByQuantity = 0;
        double expenseByValueBefore = 0;
        int expenseByQuantityBefore = 0;
        int yourPayment=0;
        List<Payment> pendingPayment;
        int sumPendingPayment = 0;


        //totalExpenseByValue
//        for(Expense expense:dExpense){
//            Date expenseDate = new Date(expense.getCreatedDate());
//            int monthExpense=expenseDate.getMonth();
//
//            if(monthExpense==monthNow){
//                expenseByValue+=expense.getPrice();
//                expenseByQuantity+=expense.getQuantity();
//            }
//            else if (monthExpense==monthNow-1){
//                expenseByValueBefore+=expense.getPrice();
//                expenseByQuantityBefore+=expense.getQuantity();
//            }
//        }
       /* if(expenseByValue||ex)
        if (expenseByValue>expenseByValueBefore)
            expenseByValuePercent= (float) (((expenseByValue/expenseByValueBefore)-1)*100);
        else if (expenseByValue<expenseByValueBefore)
            expenseByValuePercent= (float) (((expenseByValueBefore/expenseByValue)-1)*(100)*-1);
        else
            expenseByValuePercent=0;


        if (expenseByQuantity>expenseByQuantityBefore)
            expenseByQuantityPercent=(((expenseByQuantity/expenseByQuantityBefore)-1)*100);
        else if (expenseByQuantity<expenseByQuantityBefore)
            expenseByQuantityPercent=(((expenseByQuantityBefore/expenseByQuantity)-1)*(100)*-1);
        else
            expenseByQuantityPercent=0;*/


        //PendingPayment
        pendingPayment=paymentRepository.findByEmailAndIsPaid(dUser.getEmail(),true);
        for(Payment payment:pendingPayment){
            sumPendingPayment+=payment.getPrice();
        }

        //youtPayment
        Date joinDates = new Date(dUser.getJoinDate());
        int joinDate=joinDates.getMonth();

        yourPayment+=joinDate+dUser.getTotalPeriodPayed()%12;


        Dashboard d = new Dashboard();
        d.setGroupBalance(dGroup.getGroupBalance());
        d.setTotalMembers(totalMembers);
        d.setAdminAccountNumber(accountNumber);
        d.setAdminName(adminName);
        d.setExpenseByQuantity(expenseByQuantity);
        d.setExpenseByValue(expenseByValue);
        d.setExpenseByQuantityBefore(expenseByQuantityBefore);
        d.setExpenseByValueBefore(expenseByValueBefore);
        d.setYourContribution(dUser.getBalance());
        d.setYourPayment(yourPayment);
        d.setPendingPayment(sumPendingPayment);

        return d;
    }
}


//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(where("name").is(dUser.getGroupName())),
//                Aggregation.project().and("member").project("size").as("count"));
//        d.setTotalMembers(dGroup.getMember().size());
