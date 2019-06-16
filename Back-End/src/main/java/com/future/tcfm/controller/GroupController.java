package com.future.tcfm.controller;

import com.future.tcfm.model.Group;
import com.future.tcfm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{groupName}")
    public Group getGroup(@PathVariable("groupName") String groupName) {
        return groupService.getGroup(groupName);
    }

    @PostMapping
    public ResponseEntity createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable("id") String id, @RequestBody Group group) {
        return groupService.updateGroup(id,group);
    }
}
