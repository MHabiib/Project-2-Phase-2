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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;

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
        Long latestExpense = expenseRepository.findTopByGroupNameAndStatusOrderByLastModifiedAtDesc(userGroup.getName(),true).getLastModifiedAt();
        if(latestExpense==null){
            latestExpense = 0L;
        }
        int paymentPaidThisMonth = userRepository.countByGroupNameAndPeriodeTertinggalLessThanAndActive(userGroup.getName(),1,true);
        Overview overviewData = new Overview();
        overviewData.setLatestExpense(listExpense);
        overviewData.setGroupBalance(userGroup.getGroupBalance());
        overviewData.setTotalMembers(totalUser);
        overviewData.setPercentageTotalCashUsed(getPercentageTotalCashUsed(userGroup,totalUser));
        overviewData.setPaymentPaidThisMonth(paymentPaidThisMonth);
        overviewData.setLatestJoinDate(System.currentTimeMillis());
        overviewData.setLatestExpenseDate(latestExpense);
        return overviewData;
    }

    private double getPercentageTotalCashUsed(Group groupExist,int totalMembers){
        long selisihBulanDalamMs = System.currentTimeMillis()-groupExist.getCreatedDate();
        int selisihBulan = (int)(selisihBulanDalamMs/2.628e+9)+1;
        double saldoSekarangSeharusnya = selisihBulan*groupExist.getRegularPayment()*totalMembers;
        double result = groupExist.getBalanceUsed()/saldoSekarangSeharusnya;
        return Double.parseDouble(new DecimalFormat("##.##").format(result))*100;
    }
}
