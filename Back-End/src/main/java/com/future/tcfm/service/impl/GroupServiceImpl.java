package com.future.tcfm.service.impl;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.GroupService;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    @Override
    public List<Group> loadAll() {
        return groupRepository.findAll();
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
//  --- Get Members List by Email ---

    /**
     * paging
     * @param email,page,size
     * @return
     */
    @Override
    public Page<User> findMembersGroupByEmail(String email, int page, int size) {
        String userGroup = userRepository.findByEmail(email).getGroupName();
        return userRepository.findByGroupName(userGroup,createPageRequest(page,size));
    }
    @Override
    public ResponseEntity<?> createGroup(Group group) {
        Group groupExist = groupRepository.findByName(group.getName());
        if (groupExist != null)
            return new ResponseEntity<>("Failed to save Group!\nName already exists!", HttpStatus.BAD_REQUEST);
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
}
