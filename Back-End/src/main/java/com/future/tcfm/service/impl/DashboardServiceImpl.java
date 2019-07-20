package com.future.tcfm.service.impl;

import com.future.tcfm.model.Dashboard;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final
    GroupRepository groupRepository;

    private final
    UserRepository userRepository;

    @Autowired
    public DashboardServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Dashboard getData(String email) {
        User dUser = userRepository.findByEmail(email);
        Group dGroup = groupRepository.findByName(dUser.getGroupName());
        Integer totalMembers = userRepository.countByGroupName(dGroup.getName());

        User groupAdmin = userRepository.findByGroupNameAndRole(dUser.getGroupName(), "GROUP_ADMIN");
        String adminName = groupAdmin.getName();
        String accountNumber = groupAdmin.getRekening();

        Dashboard d = new Dashboard();
        d.setGroupBalance(dGroup.getGroupBalance());
        d.setTotalMembers(totalMembers);
        d.setAdminAccountNumber(accountNumber);
        d.setAdminName(adminName);

        return d;
    }
}


//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(where("name").is(dUser.getGroupName())),
//                Aggregation.project().and("member").project("size").as("count"));
//        d.setTotalMembers(dGroup.getMember().size());
