/*
package com.future.tcfm.seeder;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if(userRepository.findByEmail("momo@jyp.com")==null||userRepository.findByEmail("sana@jyp.com")==null||groupRepository.findByName("BDZ")==null||groupRepository.findByName("LOA")==null||expenseRepository.findAll()==null){
            cleanUp();
            createUsers();
            createGroups();
            createExpense();
        }
    }

    private void createUsers(){
        final User user1 = new User(null,null,null,null,null,null,null,null,null,null,null,null,null);
        user1.setEmail("momo@jyp.com");
        user1.setName("Momo");
        user1.setPassword(passwordEncoder.encode("momo123"));
        user1.setRole("Admin");
        user1.setPhone("082114045");
        user1.setBalance((double) 1000000000);
        user1.setActive(true);
        user1.setJoinDate((long) 1);
        user1.setLeaveDate(null);
        user1.setGroupName("LOA");
        user1.setRekening("Rekening");
        user1.setPeriodPayed(null);
        userRepository.save(user1);

        final User user2 = new User(null,null,null,null,null,null,null,null,null,null,null,null,null);
        user2.setEmail("sana@jyp.com");
        user2.setName("Sana");
        user2.setPassword(passwordEncoder.encode("sana123"));
        user2.setRole("Employee");
        user2.setPhone("082114022");
        user2.setBalance((double) 11000);
        user2.setActive(true);
        user2.setJoinDate((long) 1);
        user2.setLeaveDate(null);
        user2.setGroupName("BDZ");
        user2.setRekening("Rekening");
        user2.setPeriodPayed(null);
        userRepository.save(user2);

    }
    private void createGroups(){
        final Group group1 = new Group();
        group1.setName("BDZ");
        group1.setRegularPayment((double) 10000);
        group1.setCreatedDate(null);
        group1.setClosedDate(null);
        group1.setGroupBalance(1000000);
        group1.setExpenseList(null);
        group1.setMember(null);
        groupRepository.save(group1);

        final Group group2 = new Group();
        group2.setName("LOA");
        group2.setRegularPayment((double) 20000);
        group2.setCreatedDate(null);
        group2.setClosedDate(null);
        group2.setGroupBalance(21000000);
        group2.setExpenseList(null);
        group2.setMember(null);
        groupRepository.save(group2);

    }
    private void createExpense(){
        final Expense expense1 = new Expense();
        expense1.setDetail("Fashion");
        expense1.setPrice((double) 10000);
        expense1.setContributorList(null);
        expenseRepository.save(expense1);

        final Expense expense2 = new Expense();
        expense2.setDetail("Sound");
        expense2.setPrice((double) 20000);
        expense2.setContributorList(null);
        expenseRepository.save(expense2);
    }
    private void cleanUp() {
        userRepository.deleteAll();
    }

}
*/
