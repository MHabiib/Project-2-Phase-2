package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.CheckPeriodPayedUserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
public class CheckPeriodPayedUserServiceImpl implements CheckPeriodPayedUserService {

    @Autowired
    UserRepository userRepository;
    public int getTotalPeriodPayed(String email){
        User user = userRepository.findByEmail(email);

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
        return user.getTotalPeriodPayed()-period;
    }
}
