package com.future.tcfm.controller;

import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

        @Autowired
        UserService memberService;

        @GetMapping("/member")
        public List<Member> loadAll (){
            return memberService.loadAll();
        }

        @PostMapping("/member/create")
        public Member createMember(@RequestBody Member member) {
            return memberService.createMember(member);
        }

        @PutMapping("/member/{id}")
        public ResponseEntity<Member> updateMember(@PathVariable("id") int id, @RequestBody Member member) {
            return memberService.updatedmember(id,member);
        }
    }
}
