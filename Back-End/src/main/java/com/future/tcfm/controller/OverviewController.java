package com.future.tcfm.controller;

import com.future.tcfm.model.Overview;
import com.future.tcfm.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/overview")
public class OverviewController {
    @Autowired
    OverviewService overviewService;

    @GetMapping
    public Overview getData(@RequestParam("email") String email) {
        return overviewService.getData(email);
    }
}
