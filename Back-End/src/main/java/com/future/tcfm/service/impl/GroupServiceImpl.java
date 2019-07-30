package com.future.tcfm.service.impl;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.GroupService;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.future.tcfm.service.impl.NotificationServiceImpl.GROUP_PROFILE_UPDATE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_GROUP;
import static com.future.tcfm.service.impl.ExpenseServiceImpl.createPageRequest;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    EmailService emailService;

    @Override
    public List<Group> loadAll() {
        return groupRepository.findAll();
    }

    @Override
    public ResponseEntity getGroupById(String id) {
        Group groupExist= groupRepository.findByIdGroup(id);
        if(groupExist==null) return new ResponseEntity<>("Group not found!",HttpStatus.NOT_FOUND);
        return new ResponseEntity(groupExist,HttpStatus.OK);
    }

    @Override
    public List<User> membersGroup(String groupName) {
        return userRepository.findByGroupNameLike(groupName);
    }

//  EMAIL -> GET GROUP NAME -> GET ALL MEMBER IN THE GROUP; Robin
    @Override
    public List<User> membersGroupByEmail(String email) {
        String userGroup = userRepository.findByEmail(email).getGroupName();
        return userRepository.findByGroupNameLike(userGroup);
    }

    @Override
    public int getGroupCreatedMonth(String groupName) {
        Group groupExist = groupRepository.findByNameAndActive(groupName, true);
        if (groupExist == null)
            throw new RuntimeException("GROUP NOT FOUND!");
        return Instant.ofEpochMilli(groupExist.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
    }
    /**
     * paging
     * @param email,page,size
     * @return
     */
    @Override
    public Page<User> findMembersGroupByEmail(String email,String filter,int year, int page, int size) {
        String userGroup = userRepository.findByEmail(email).getGroupName();
        return userRepository.findByGroupName(userGroup,createPageRequest(filter,"",page,size));
    }
    @Override
    public ResponseEntity<?> createGroup(Group group) {
        Group groupExist = groupRepository.findByNameAndActive(group.getName(),true);
        if (groupExist != null &&groupExist.getActive().equals(false))
            return new ResponseEntity<>("Failed to save Group!\nName already exists!", HttpStatus.BAD_REQUEST);
        group.setBalanceUsed(0.0);
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateGroup(String id, Group group) {
        Group groupExist = groupRepository.findByIdGroup(id);
        if (groupExist == null)
            return new ResponseEntity<>("Failed to update group!\nGroupId not found!", HttpStatus.NOT_FOUND);
        groupExist.setRegularPayment(group.getRegularPayment());
        groupExist.setGroupBalance(group.getGroupBalance());
        groupExist.setActive(group.getActive());
        groupExist.setCreatedDate(new Date().getTime());
        groupRepository.save(groupExist);
        notificationService.createNotification(GROUP_PROFILE_UPDATE,null,groupExist.getName(),TYPE_GROUP);
        return new ResponseEntity<>(groupExist, HttpStatus.OK);
    }

    @Override
    public ResponseEntity disbandGroup(String id) {
        Group groupExist = groupRepository.findByIdGroup(id);
//        boolean hasEveryOnePayed = true;
        if (groupExist == null) {
            return new ResponseEntity<>("Failed to disband group!\nGroupId not found!", HttpStatus.NOT_FOUND);
        }
        List<User> userList = userRepository.findByGroupNameAndActive(groupExist.getName(), true);
        for (User user : userList) {
            if (user.getPeriodeTertinggal() > 0) {
//                hasEveryOnePayed = false;
                return new ResponseEntity<>("Failed to disband group!\nGroupId not found!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }//x

        groupExist.setActive(false);
        /**
         * tambahkan fungsi utk send email berisi data payment tiap-tiap member dan balance mereka disini.
         */
        return new ResponseEntity<>("Group is disbanded!",HttpStatus.OK);
    }


}
