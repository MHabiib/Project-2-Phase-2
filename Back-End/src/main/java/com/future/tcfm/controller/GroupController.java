package com.future.tcfm.controller;

import com.future.tcfm.model.Group;
import com.future.tcfm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping("/group")
    public List<Group> loadAll (){
        return groupService.loadAll();
    }

    @PostMapping("/group")
    public ResponseEntity createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/group/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable("id") String id, @RequestBody Group group) {
        return groupService.updateGroup(id,group);
    }
}
