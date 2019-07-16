package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
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

@Service
public class SchedulerServiceImpl implements SchedulerService {
    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    public void scheduler() throws MessagingException {
        List<User> listUser = userRepository.findAll();

        for (User user : listUser) {
            String year="";
            String month = "";
            // String day="";

            int yearNow= LocalDate.now().getYear();
            int monthNow= LocalDate.now().getMonthValue();
            int period = 0;

            Date date = new Date(user.getJoinDate());
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy MM dd");
            String dateText = df2.format(date);

            String[] words=dateText.split("\\s");//splits the string based on whitespace
            for(int i= 0; i <3;i++){
                if(i==0){year=words[i]; }
                else if(i==1){ month=words[i]; }
                //  else { day=words[i]; }
            }
            int year1= Integer.parseInt(year);
            int month1=Integer.parseInt(month);
            // int day1=Integer.parseInt(day);

            if(year1!=yearNow){
                int range = yearNow-year1;
                period+=range*12-month1+monthNow;
            }
            else{
                period+=monthNow-month1;
            }
            int range = user.getPeriodPayed().size()-period;
            String monthNowStr=Month.of(monthNow).getDisplayName(TextStyle.FULL,Locale.ENGLISH);
            int monthBefore= monthNow-range;
            if(monthBefore<1){{ monthBefore+=12; } }
            String monthBeforeStr=Month.of(monthBefore).getDisplayName(TextStyle.FULL,Locale.ENGLISH);

            if(period<user.getPeriodPayed().size()){
               emailService.periodicMailSender(user.getEmail(),range);
               if(range>1)
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_GROUP);
               else
                    notificationService.createNotification("Anda Belum Membayar Iuran Bulan "+monthBeforeStr+" - "+monthNowStr, user.getEmail(),user.getGroupName(),TYPE_GROUP);
            }
            else{
                emailService.periodicMailSender(user.getEmail(),range);
                notificationService.createNotification("Anda Telah Membayar Iuran Bulan "+monthNowStr , user.getEmail(),user.getGroupName(),TYPE_GROUP);
            }

                //emailService.sendMailWithUsername(user.getEmail(),user.getName());
        }

    }
}