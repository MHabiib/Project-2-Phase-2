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
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.future.tcfm.service.impl.NotificationServiceImpl.PAYMENT_LATE;
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

    private ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();

//
    @Async
    @Transactional
    @Scheduled(cron = "0 10 10 05 * ?") // setiap tanggal 10  disetiap bulan jam 10 : 05
    public void scheduler() throws MessagingException {
        List<User> listUser = userRepository.findAll();
        Map<String,Group> groupMap = new HashMap<>();

        listUser.forEach(user -> groupMap.put(user.getGroupName(),groupRepository.findByName(user.getGroupName())));
        int monthNow = LocalDate.now().getMonthValue();
        String monthNowStr=Month.of(monthNow).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        groupMap.forEach((groupName,groupVal)->{
            groupVal.setCurrentPeriod(groupVal.getCurrentPeriod()+1); //misalkan sudah berganti bulan, maka update period group
            groupRepository.save(groupVal);
        });

        sseMvcExecutor.execute(() -> {//pisahThread
            int yearBefore = LocalDate.now().getYear();
            int monthChecker = 0;
            int yearChecker=0;
            int monthBefore = LocalDate.now().getMonthValue();
            Group group;
            String monthBeforeStr = "";//untuk mendapatkan value bulan yang belum dibayar user

            for (User user : listUser) {
                group = groupMap.get(user.getGroupName());
                user.setPeriodeTertinggal(group.getCurrentPeriod() - user.getTotalPeriodPayed());
                if (user.getPeriodeTertinggal() > 0) {
                    if(user.getPeriodeTertinggal()>11){
                        monthChecker=user.getPeriodeTertinggal() % 12;
                        yearChecker=(user.getPeriodeTertinggal()-monthChecker)/12;
                        monthBefore-=monthChecker;
                        yearBefore-=yearChecker;
                    }
                    else{
                        monthBefore-=user.getPeriodeTertinggal();
                    }
                    if(monthBefore<=0){
                        monthBefore+=12;
                        yearBefore-=1;
                    }
                    monthBeforeStr=Month.of(monthBefore).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
//                notificationService.createNotification("You haven't made any payment from "+ monthBeforeStr+" to "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_PERSONAL);
                    notificationService.createNotification("You have missed " + user.getPeriodeTertinggal() + "'s month payment", user.getEmail(), user.getGroupName(), TYPE_PERSONAL);
                    try {
                        emailService.periodicMailSender(user.getEmail(), monthBeforeStr,yearBefore);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                } else {
                    notificationService.createNotification("Thank you for completing " + monthNowStr + "'s payment", user.getEmail(), user.getGroupName(), TYPE_PERSONAL);
                    try {
                        emailService.periodicMailSender(user.getEmail(), monthBeforeStr,yearBefore);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
            userRepository.saveAll(listUser);
        });
    }

    @Async
    @Transactional
    @Scheduled(cron = "0 7 10 05 * ?") // setiap tanggal 7 disetiap bulan jam 10 : 05
    public void schedulerReminder() throws MessagingException {
        List<User> listUser = userRepository.findAll();
        sseMvcExecutor.execute(() -> {//pisahThread
            for(User user:listUser){
                try {
                    emailService.periodicMailReminderSender(user.getEmail());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Async
    @Transactional
    @Scheduled(cron = "0 31 10 05 * ?") // setiap tanggal 31 disetiap bulan jam 10 : 05
    public void monthlyCashStatemen() throws MessagingException {
        List<User> listUser = userRepository.findAll();
        sseMvcExecutor.execute(() -> {//pisahThread
            for(User user:listUser){
                try {
                    emailService.periodicMailReminderSender(user.getEmail());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    @Async
////    @Scheduled(fixedRate = 5000L)
//    @Transactional
//    public void checkUserPeriodPaid(){
//        System.out.println("This is the checkUserPeriodPaid method!");
//        List<User> userList = userRepository.findAllByActive(true);
//        Map<String,Group> groupMap = new HashMap<String,Group>();
//        userList.forEach(user -> groupMap.put(user.getGroupName(),groupRepository.findByName(user.getGroupName())));
//        Group group;
//        String message=PAYMENT_LATE;
//        for (User user : userList) {
//            group = groupMap.get(user.getGroupName());
//            if(user.getTotalPeriodPayed()<group.getCurrentPeriod()){
//                user.setPeriodeTertinggal(group.getCurrentPeriod()-user.getTotalPeriodPayed());
//                notificationService.createNotification(message,user.getEmail(),null,TYPE_PERSONAL);
//            }
//        }
//        userRepository.saveAll(userList);
//    }
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
