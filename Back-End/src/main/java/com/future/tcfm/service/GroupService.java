package com.future.tcfm.service;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    List<Group> loadAll();
    ResponseEntity createGroup(Group group);
    ResponseEntity updateGroup(String id,Group group);
    List<User> membersGroup(String groupName);
    List<User> membersGroupByEmail(String email);
}
