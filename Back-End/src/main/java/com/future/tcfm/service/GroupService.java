package com.future.tcfm.service;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GroupService {
    List<Group> loadAll();
    ResponseEntity createGroup(Group group);
    ResponseEntity updateGroup(String id,Group group);
    List<User> membersGroup(String groupName);
}
