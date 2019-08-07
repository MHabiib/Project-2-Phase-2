package com.future.tcfm.service.impl;

import com.future.tcfm.model.*;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import com.future.tcfm.service.GroupService;
import com.future.tcfm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;
import static com.future.tcfm.service.impl.NotificationServiceImpl.GROUP_PROFILE_UPDATE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.TYPE_GROUP;
import static com.future.tcfm.service.impl.ExpenseServiceImpl.createPageRequest;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUserDetailsRepository jwtUserDetailsRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    EmailService emailService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Group> loadAll() {
        return groupRepository.findAll();
    }

    @Override
    public ResponseEntity getGroupById(String id) {
        Group groupExist= groupRepository.findByIdGroup(id);
        if(groupExist==null) return new ResponseEntity<>("Group not found!",HttpStatus.NOT_FOUND);
        return new ResponseEntity(groupExist,HttpStatus.OK);
    }

    @Override
    public List<User> membersGroup(String groupName) {
        return userRepository.findByGroupNameAndActiveOrderByNameAsc(groupName,true);
    }

//  EMAIL -> GET GROUP NAME -> GET ALL MEMBER IN THE GROUP; Robin
    @Override
    public List<User> membersGroupByEmail(String email) {
        String userGroup = userRepository.findByEmail(email).getGroupName();
        return userRepository.findByGroupNameAndActive(userGroup,true);
    }

    @Override
    public int getGroupCreatedMonth(String groupName) {
        Group groupExist = groupRepository.findByNameAndActive(groupName, true);
        if (groupExist == null)
            throw new RuntimeException("GROUP NOT FOUND!");
        return Instant.ofEpochMilli(groupExist.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
    }
    /**
     * paging
     * @param email,page,size
     * @return
     */
    @Override
    public Page<User> findMembersGroupByEmail(String email,String filter,int year, int page, int size) {
        String userGroup = userRepository.findByEmail(email).getGroupName();
        return userRepository.findByGroupName(userGroup,createPageRequest(filter,"",page,size));
    }
    @Override
    public ResponseEntity<?> createGroup(Group group) {
        Group groupExist = groupRepository.findByNameAndActive(group.getName(),true);
        if (groupExist != null &&groupExist.getActive().equals(false))
            return new ResponseEntity<>("Failed to save Group!\nName already exists!", HttpStatus.BAD_REQUEST);
        if(group.getCurrentPeriod()==0){
            group.setCurrentPeriod(1);
        }if(group.getGroupAdmin()==null){
            group.setGroupAdmin("");
        }
        group.setGroupAdmin(group.getGroupAdmin().equalsIgnoreCase("")?"":group.getGroupAdmin());
        group.setCreatedDate(System.currentTimeMillis());
        group.setLastModifiedAt(System.currentTimeMillis());
        group.setClosedDate(0L);
        group.setActive(true);
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity updateGroup(String id, Group group) {
        Group groupExist = groupRepository.findByIdGroup(id);
        List<User> userList;
        List<Expense> expenseList;
        if (groupExist == null)
            return new ResponseEntity<>("Failed to update group!\nGroupId not found!", HttpStatus.NOT_FOUND);
        Boolean isNameAvailable = groupRepository.countAllByNameAndActive(group.getName(), true) == 0;
        groupExist.setRegularPayment(group.getRegularPayment());
        if(!groupExist.getGroupAdmin().equalsIgnoreCase(group.getGroupAdmin())){
            User newAdmin = userRepository.findByEmailAndActive(group.getGroupAdmin(),true);
            if(!groupExist.getGroupAdmin().equalsIgnoreCase("")) {
                User oldAdmin = userRepository.findByEmail(groupExist.getGroupAdmin());//gk pakai active karena bisa saja admin lama udh resign
                groupExist.setGroupAdmin(newAdmin.getEmail());
                oldAdmin.setRole("MEMBER");
                userRepository.save(oldAdmin);
            }
            newAdmin.setRole("GROUP_ADMIN");
            userRepository.save(newAdmin);
        }
        groupExist.setGroupBalance(group.getGroupBalance());
        groupExist.setBalanceUsed(group.getBalanceUsed());
        groupExist.setBankAccountNumber(group.getBankAccountNumber());
        groupExist.setLastModifiedAt(System.currentTimeMillis());
        groupExist.setCurrentPeriod(group.getCurrentPeriod());
        if(isNameAvailable) {
            expenseList = expenseRepository.findByGroupNameLikeOrderByCreatedDateDesc(groupExist.getName());
            userList = userRepository.findByGroupNameAndActive(groupExist.getName(),true);
            jwtUserDetailsRepository.deleteAllByGroupName(groupExist.getName());
            groupExist.setName(group.getName());
            expenseList.forEach(expense -> expense.setGroupName(groupExist.getName()));
            userList.forEach(user -> user.setGroupName(groupExist.getName()));
            expenseRepository.saveAll(expenseList);
            userRepository.saveAll(userList);
        }
        groupRepository.save(groupExist);
        notificationService.createNotification(GROUP_PROFILE_UPDATE+getCurrentUser().getEmail(),null,groupExist.getName(),TYPE_GROUP);
        return new ResponseEntity<>(groupExist, HttpStatus.OK);
    }

    @Override
    public ResponseEntity disbandGroup(String id) {
        Group groupExist = groupRepository.findByIdGroup(id);
        List<User> memberHasNotPaid = new ArrayList<>();
        if (groupExist == null) {
            return new ResponseEntity<>("Failed to disband group!\nGroupId not found!", HttpStatus.NOT_FOUND);
        }
        List<User> userList = userRepository.findByGroupNameAndActiveOrderByNameAsc(groupExist.getName(), true);
        for (User user : userList) {
            if (user.getPeriodeTertinggal() > 0) {
                memberHasNotPaid.add(user);
            }
        }
        System.out.println("Member hasnt paid : "+memberHasNotPaid);
        if(memberHasNotPaid.size()>0){
            return new ResponseEntity<>(memberHasNotPaid, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        groupExist.setActive(false);
        groupRepository.save(groupExist);
        /**
         * tambahkan fungsi utk send email berisi data payment tiap-tiap member dan balance mereka disini.
         */
        return new ResponseEntity<>("Group just been disbanded!",HttpStatus.OK);
    }

    @Override
    public Page<Group> searchBy(String query, int page, int size) throws ParseException {
        String groupName = getCurrentUser().getGroupName();
        System.out.println("Query Param : "+query);
        Pattern pattern = Pattern.compile("(.*)(:)(.*)");
        Matcher matcher = pattern.matcher(query);
        if(!matcher.find()){return null;}
        String key=matcher.group(1);
        String value=matcher.group(3);
        System.out.println("Key : "+key+"; Value : "+value);
        Pageable pageable = createPageRequest(key,"asc",page,size);
        Query myQuery = new Query().with(pageable);
        Criteria criteria = Criteria.where(key).regex(value,"i").and("active").is(true);
        if(key.equalsIgnoreCase("date before")){
            key = "createdDate";
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            long timeStamp= System.currentTimeMillis();
            try {
                timeStamp = formatter.parse(value).getTime();
            }catch (Exception e){e.printStackTrace();}
            criteria = Criteria.where(key).lte(timeStamp).and("active").is(true);
        }else if(key.equalsIgnoreCase("date after")) {
            key = "createdDate";
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            long timeStamp = 0;
            try {
                timeStamp = formatter.parse(value).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
            criteria = Criteria.where(key).gte(timeStamp).and("active").is(true);
        } else if(key.equalsIgnoreCase("balance <")) {
            key="groupBalance";
            double dValue = value.equalsIgnoreCase("")? Double.MAX_VALUE :Double.parseDouble(value);
            criteria = Criteria.where(key).lt(dValue).and("active").is(true);
        }else if(key.equalsIgnoreCase("balance >")) {
            key="groupBalance";
            double dValue = value.equalsIgnoreCase("")? 0 :Double.parseDouble(value);
            criteria = Criteria.where(key).gt(dValue).and("active").is(true);
        }
        myQuery.addCriteria(criteria);
        List<Group> paymentList =  mongoTemplate.find(myQuery,Group.class,"group");
        return PageableExecutionUtils.getPage(
                paymentList,
                pageable,
                () -> mongoTemplate.count(myQuery, Group.class));
    }

}
