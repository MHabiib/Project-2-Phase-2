package com.future.tcfm.controller;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.OverviewService;
import com.future.tcfm.service.SchedulerService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@CrossOrigin
@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    @Autowired
    SchedulerService schedulerService;

    //@Scheduled(cron = "0 28 10 05 * ?") // setiap tanggal 28  disetiap bulan jam 10 : 05
    @Scheduled(fixedRate = 30000)
    public void scheduler() throws MessagingException {
       schedulerService.scheduler();
    }
}

