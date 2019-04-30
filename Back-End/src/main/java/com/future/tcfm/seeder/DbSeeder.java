package com.future.tcfm.seeder;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder {
    @Autowired
    private UserRepository userRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        createUsers();
    }
    
    public void createUsers() {
        if(userRepository.findByEmail("momo@jyp.com")==null){
            cleanUp();

            final User user1= new User(null,null,null,null,null,null,null,null,null,null,null,null,null);
            user1.setEmail("momo@jyp.com");
            user1.setName("Momo");
            user1.setPassword("momo123");
            user1.setRole("Admin");
            user1.setPhone("082114045");
            user1.setBalance((double) 1000000000);
            user1.setActive(true);
            user1.setJoinDate((long) 1);
            user1.setLeaveDate(null);
            user1.setRekening("Rekening");
            user1.setPeriodPayed(null);
            userRepository.save(user1);

            final User user2 = new User(null,null,null,null,null,null,null,null,null,null,null,null,null);
            user2.setEmail("sana@jyp.com");
            user2.setName("Sana");
            user2.setPassword("sana123");
            user2.setRole("Employee");
            user2.setPhone("082114022");
            user2.setBalance((double) 11000);
            user2.setActive(true);
            user2.setJoinDate((long) 1);
            user2.setLeaveDate(null);
            user2.setRekening("Rekening");
            user2.setPeriodPayed(null);
            userRepository.save(user2);
        }
    }
    public void cleanUp() {
        userRepository.deleteAll();
    }

}
