package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Overview;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String userGroup = userRepository.findByEmail(email).getGroupName();
        List<Expense> listExpense = expenseRepository.findTop10ByGroupNameLikeOrderByCreatedDateDesc(userGroup);
        Double groupBalance = groupRepository.findByName(userRepository.findByEmail(email).getGroupName()).getGroupBalance();
        Integer totalUser = userRepository.findByGroupNameLike(userGroup).size();
        Long latestJoinDate = userRepository.findByGroupNameLikeOrderByJoinDateDesc(userGroup).get(0).getJoinDate();
        Long latestExpense = expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(userGroup).get(0).getCreatedDate();

        Overview overviewData = new Overview();
        overviewData.setLatestExpense(listExpense);
        overviewData.setGroupBalance(groupBalance);
        overviewData.setTotalMembers(totalUser);
        overviewData.setLatestJoinDate(latestJoinDate);
        overviewData.setLatestExpenseDate(latestExpense);
        return overviewData;
    }
}
