package com.future.tcfm.controller;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping
    public List<Group> loadAll (){
        return groupService.loadAll();
    }

    @GetMapping("/members") //body fill with group name without ""
    public List<User> membersGroup(@RequestBody String groupName){
        return groupService.membersGroup(groupName);
    }

    @GetMapping("/{id}") // ini seharusnya gk usah, cukup @GetMapping aja gmn? biar jadi /api/user?email=value
    public ResponseEntity getMemberById(@PathVariable("id") String id) {
        return groupService.getGroupById(id);
    }

    @PostMapping
    public ResponseEntity createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable("id") String id, @RequestBody Group group) {
        return groupService.updateGroup(id,group);
    }
    @GetMapping("/membersByEmail")
    public Page<User> findMyGroupMembers(@RequestParam("email") String email,
                                         @RequestParam(value = "filter",required = false, defaultValue = "name") String filter,
                                         @RequestParam(value = "year",required = false, defaultValue = "0") int year,
                                         @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size",required = false, defaultValue = "10") int size) {
        return groupService.findMembersGroupByEmail(email,filter,year,page,size);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Group> disbandGroup(@PathVariable("id") String id) {
        return groupService.disbandGroup(id);
    }

}
