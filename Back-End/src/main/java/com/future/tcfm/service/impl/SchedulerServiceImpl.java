package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.CheckPeriodPayedUserService;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_GROUP;
import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_PERSONAL;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CheckPeriodPayedUserService checkPeriodPayedUserService;

    public void scheduler() throws MessagingException {
        List<User> listUser = userRepository.findAll();

        for (User user : listUser) {

            int yearNow= LocalDate.now().getYear();
            int monthNow= LocalDate.now().getMonthValue();
            int period = 0;

            int range=user.getPeriodeTertinggal();

            String monthNowStr=Month.of(monthNow).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
            int monthBefore= monthNow-range;
            if(monthBefore<1){{ monthBefore+=12; } }
            String monthBeforeStr=Month.of(monthBefore).getDisplayName(TextStyle.FULL,Locale.ENGLISH);

            emailService.userResign(user.getEmail());
            /*if(period<user.getTotalPeriodPayed()){
               emailService.periodicMailSender(user.getEmail(),range);
               if(range>1)
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_GROUP);
               else
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthBeforeStr+" - "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_PERSONAL);
            }
            else{
                emailService.periodicMailSender(user.getEmail(),range);
                notificationService.createNotification("Anda Telah Membayar Iuran Bulan "+monthNowStr , user.getEmail(),user.getGroupName(),TYPE_PERSONAL);
            }*/
                //emailService.sendMailWithUsername(user.getEmail(),user.getName());
        }

    }
}
