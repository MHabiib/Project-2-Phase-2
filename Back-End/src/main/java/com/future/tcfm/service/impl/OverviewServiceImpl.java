package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.ReqResModel.Overview;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OverviewServiceImpl implements OverviewService {
    @Autowired
    public OverviewServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    private final
    ExpenseRepository expenseRepository;

    private final
    UserRepository userRepository;

    private final
    GroupRepository groupRepository;

    @Override
    public Overview getData(String email) {
        User dUser = userRepository.findByEmail(email);
        Group userGroup = groupRepository.findByNameAndActive(dUser.getGroupName(),true);
        List<Expense> listExpense = expenseRepository.findTop10ByGroupNameOrderByCreatedDateDesc(userGroup.getName());
        if (listExpense == null) {
            listExpense = new ArrayList<>();
        }
        int totalUser = userRepository.countAllByGroupNameAndActive(userGroup.getName(),true);
        Expense latestExpense = expenseRepository.findTopByGroupNameAndStatusOrderByLastModifiedAtDesc(userGroup.getName(),true);
        if(latestExpense==null){
            latestExpense = new Expense();
        }
        int paymentPaidThisMonth = userRepository.countByGroupNameAndPeriodeTertinggalLessThanAndActive(userGroup.getName(),1,true);
        Overview overviewData = new Overview();
        overviewData.setLatestExpense(listExpense);
        overviewData.setGroupBalance(userGroup.getGroupBalance());
        overviewData.setTotalMembers(totalUser);
        overviewData.setPercentageTotalCashUsed(getPercentageTotalCashUsed(userGroup,totalUser));
        overviewData.setPaymentPaidThisMonth(paymentPaidThisMonth);
        overviewData.setLatestJoinDate(System.currentTimeMillis());
        overviewData.setLatestExpenseDate(latestExpense.getLastModifiedAt());
        return overviewData;
    }

    private String getPercentageTotalCashUsed(Group groupExist, int totalMembers){
/*
        long selisihBulanDalamMs = System.currentTimeMillis()-groupExist.getCreatedDate();
        int selisihBulan = (int)(selisihBulanDalamMs/2.628e+9)+1;
        double saldoSekarangSeharusnya = selisihBulan*groupExist.getRegularPayment()*totalMembers;

*/


        double result = groupExist.getBalanceUsed()/groupExist.getGroupBalance();
//test
        DecimalFormat df=new DecimalFormat("##.##");
        return df.format(result*100);
        //return Double.parseDouble(new DecimalFormat("##.##").format(result))*100;
    }
}
