package com.future.tcfm.service.impl;

import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.text.DateFormatSymbols;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.future.tcfm.service.impl.NotificationServiceImpl.PAYMENT_LATE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_GROUP;
import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_PERSONAL;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    NotificationService notificationService;

    ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();

    @Async
    @Transactional
    public void scheduler() throws MessagingException {
        List<User> listUser = userRepository.findAll();
        Map<String,Group> groupMap = new HashMap<>();

        listUser.forEach(user -> groupMap.put(user.getGroupName(),groupRepository.findByName(user.getGroupName())));

        int monthNow= LocalDate.now().getMonthValue();


        groupMap.forEach((groupName,groupVal)->{
            groupVal.setCurrentPeriod(groupVal.getCurrentPeriod()+1); //misalkan sudah berganti bulan, maka update period group
            groupRepository.save(groupVal);
        });

        sseMvcExecutor.execute(() -> {
            Group group;
            String monthBeforeStr = "";//untuk mendapatkan value bulan yang belum dibayar user
            String monthNowStr="";
            for (User user : listUser) {
                group = groupMap.get(user.getGroupName());
//            monthBeforeStr=Month.of((monthNow-user.getPeriodeTertinggal())%12).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
                monthNowStr = Month.of(monthNow).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                user.setPeriodeTertinggal(group.getCurrentPeriod() - user.getTotalPeriodPayed());
                if (user.getPeriodeTertinggal() > 0) {
//                notificationService.createNotification("You haven't made any payment from "+ monthBeforeStr+" to "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_PERSONAL);

                    notificationService.createNotification("You have missed " + user.getPeriodeTertinggal() + "'s month payment", user.getEmail(), user.getGroupName(), TYPE_PERSONAL);
                    try {
                        emailService.periodicMailSender(user.getEmail(), monthNowStr, monthBeforeStr);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                } else {
                    notificationService.createNotification("Thank you for completing " + monthNowStr + "'s payment", user.getEmail(), user.getGroupName(), TYPE_PERSONAL);
                    try {
                        emailService.periodicMailSender(user.getEmail(), monthNowStr, monthBeforeStr);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
            userRepository.saveAll(listUser);
        });
    }

    @Async
//    @Scheduled(cron = "0 28 10 05 * ?") // setiap tanggal 28  disetiap bulan jam 10 : 05
//    @Scheduled(fixedRate = 5000L)
    @Transactional
    public void checkUserPeriodPaid(){
        System.out.println("This is the checkUserPeriodPaid method!");
        List<User> userList = userRepository.findAllByActive(true);
        Map<String,Group> groupMap = new HashMap<String,Group>();
        userList.forEach(user -> groupMap.put(user.getGroupName(),groupRepository.findByName(user.getGroupName())));
        Group group;
        String message=PAYMENT_LATE;
        for (User user : userList) {
            group = groupMap.get(user.getGroupName());
            if(user.getTotalPeriodPayed()<group.getCurrentPeriod()){
                user.setPeriodeTertinggal(group.getCurrentPeriod()-user.getTotalPeriodPayed());
                notificationService.createNotification(message,user.getEmail(),null,TYPE_PERSONAL);
            }
        }
        userRepository.saveAll(userList);
    }
}

/*

 for (User user : listUser) {

        int yearNow= LocalDate.now().getYear();
        int monthNow= LocalDate.now().getMonthValue();
        int period = 0;

*/
/*
  int range=user.getPeriodeTertinggal();

            String monthNowStr=Month.of(monthNow).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
            int monthBefore= monthNow-range;
            if(monthBefore<1){{ monthBefore+=12; } }
            String monthBeforeStr=Month.of(monthBefore).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
*//*



        emailService.userResign(user.getEmail());
*/
/*
 if(period<user.getTotalPeriodPayed()){
               //emailService.periodicMailSender(user.getEmail(),range);
                 emailService.userResign(user.getEmail());

if(range>1)
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_GROUP);
               else
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthBeforeStr+" - "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_PERSONAL);
            }
            else{
*//*

        emailService.userResign(user.getEmail());
        //    emailService.periodicMailSender(user.getEmail(),range);
*/
/*
                notificationService.createNotification("Anda Telah Membayar Iuran Bulan "+monthNowStr , user.getEmail(),user.getGroupName(),TYPE_PERSONAL);
            }
*//*



        //emailService.sendMailWithUsername(user.getEmail(),user.getName());
    }

}
*/
