package com.future.tcfm.service;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

public interface GroupService {
    List<Group> loadAll();
    Page<User> findMembersGroupByEmail(String email,String filter,int year, int page, int size);

    ResponseEntity createGroup(Group group);
    ResponseEntity updateGroup(String id,Group group);
    List<User> membersGroup(String groupName);
    List<User> membersGroupByEmail(String email);

    ResponseEntity disbandGroup(String id);

    int getGroupCreatedMonth(String groupName);

    ResponseEntity getGroupById(String id);

    Page<Group> searchBy(String query, int page, int size) throws ParseException;
}
