package com.future.tcfm.service.impl;

import com.future.tcfm.model.Group;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupRepository groupRepository;
    
    @Override
    public List<Group> loadAll() {
        return groupRepository.findAll();
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
        groupExist.setMember(group.getMember());
        return new ResponseEntity<>(groupExist, HttpStatus.OK);

    }
}
