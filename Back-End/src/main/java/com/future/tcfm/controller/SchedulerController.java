package com.future.tcfm.controller;

import com.future.tcfm.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
@CrossOrigin("**")
@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    @Autowired
    SchedulerService schedulerService;

    @Scheduled(cron = "0 28 10 05 * ?") // setiap tanggal 28  disetiap bulan jam 10 : 05
//       @Scheduled(fixedRate = 10000)
    @GetMapping("/test")
        public void scheduler() throws MessagingException {
        schedulerService.scheduler();
    }
}

