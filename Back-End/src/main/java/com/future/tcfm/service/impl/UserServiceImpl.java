package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.JwtUserDetails;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.JwtUserDetailsRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.future.tcfm.service.impl.ExpenseServiceImpl.createPageRequest;
import static com.future.tcfm.service.impl.NotificationServiceImpl.*;

@Service
public class UserServiceImpl implements UserService {
    public static final String UPLOADED_FOLDER="../assets/";
    public static final String UPLOADED_URL = "http://localhost:8088/img/";

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    NotificationService notificationService;

    @Autowired
    JwtUserDetailsRepository jwtUserDetailsRepository;

    private String notifMessage;
    @Override
    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity getUserById(String id) {
        User userExist = userRepository.findByIdUser(id);
        if(userExist==null) return new ResponseEntity<>("User not found!",HttpStatus.NOT_FOUND);
        return new ResponseEntity(userExist,HttpStatus.OK);
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public static void saveUploadedFile(MultipartFile file,String name) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER +name);
            System.out.println(UPLOADED_FOLDER+name);
            Files.write(path, bytes);
        }
    }

    public static boolean checkImageFile(MultipartFile file) {
        if (file != null) {
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                return false;
            }
            return file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/bmp");
        }
        return false;
    }
    @Override
    public ResponseEntity getImage(String imageName) throws IOException {
        Path path = Paths.get(UPLOADED_FOLDER + imageName);
        File img = new File(String.valueOf(path));
        String mimetype = FileTypeMap.getDefaultFileTypeMap().getContentType(img);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mimetype))
                .body(Files.readAllBytes(img.toPath()));
    }

    @Override
    public Page<User> searchBy(String name,String email, String groupName, String role,int page,int size) {
        return userRepository.findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndGroupNameContainingIgnoreCaseAndRoleContainingIgnoreCaseAndActiveOrderByTotalPeriodPayed(name,email,groupName,role,true,createPageRequest("totalPeriodPayed","desc",page,size));
    }

    /**
     * function yg d call ktk merubah data user yg dilakukan super_admin
     * @param id
     * @param user
     * @return
     */
    @Override
    public ResponseEntity manageUser(String id, User user){
        User userExist = userRepository.findByIdUser(id);
        Group groupExist = groupRepository.findByNameAndActive(user.getGroupName(),true);
        if(userExist==null){
            return new ResponseEntity("Username not found!", HttpStatus.NOT_FOUND);
        }
        if(!userExist.getGroupName().equals(user.getGroupName())) {
            //notification untuk group lamanya
            if(groupExist==null){
                return new ResponseEntity("Group not found!", HttpStatus.NOT_FOUND);
            }
            userExist.setJoinDate(new Date().getTime());
            userExist.setGroupName(user.getGroupName());

            notifMessage = userExist.getEmail()+USER_LEFT_GROUP;
            notificationService.createNotification(notifMessage,userExist.getEmail(),userExist.getGroupName(),TYPE_GROUP);
            //notification untuk group barunya
            notifMessage = userExist.getEmail()+USER_JOINED_GROUP;
            notificationService.createNotification(notifMessage,userExist.getEmail(),user.getGroupName(),TYPE_GROUP);
        }
        if(!userExist.getRole().equals(user.getRole())){
            if(user.getRole().equals("GROUP_ADMIN")){
                User oldAdmin = userRepository.findByEmail(groupExist.getGroupAdmin());
                oldAdmin.setRole(oldAdmin.getRole().replace("GROUP_ADMIN","MEMBER"));
                groupExist.setGroupAdmin(user.getEmail());
            }
        }
        userExist.setRole(userExist.getRole());
        userExist.setEmail(user.getEmail());
        userExist.setName(user.getName());
        userExist.setPhone(user.getPhone());
        userExist.setPassword(user.getPassword());
        userExist.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        JwtUserDetails jwtUserDetails = jwtUserDetailsRepository.findByEmail(userExist.getEmail());
        jwtUserDetailsRepository.delete(jwtUserDetails);
        userRepository.save(userExist);
        return new ResponseEntity(userExist,HttpStatus.OK);
    }

    /**
     * function untuk user utk mengupdate profilenya
     * @param id
     * @param userJSONString
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity updateUserV2(String id, String userJSONString, MultipartFile file) throws IOException {
        User user  = new ObjectMapper().readValue(userJSONString, User.class);
        User userExist = userRepository.findByIdUser(id);
        if(userExist==null){
            return new ResponseEntity("Username not found!", HttpStatus.NOT_FOUND);
        }
        if(checkImageFile(file)){
            try{
                if(userExist.getImagePath()!=null) {
                    Path deletePath = Paths.get(UPLOADED_FOLDER + userExist.getImagePath());
                    Files.delete(deletePath);
                }
                String fileName="user/"+userExist.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                userExist.setImagePath(fileName);
                userExist.setImageURL(UPLOADED_URL+fileName);
            }catch (IOException e){
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        userExist.setName(user.getName());
        userExist.setPhone(user.getPhone());
        userExist.setPassword(user.getPassword());
        userExist.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        userRepository.save(userExist);
        return new ResponseEntity(userExist,HttpStatus.OK);
    }


    @Override
    public ResponseEntity createUserV2(String userJSONString, MultipartFile file) throws IOException {
        User user  = new ObjectMapper().readValue(userJSONString, User.class);
        System.out.println(user);
        User userExist = userRepository.findByEmail(user.getEmail());
        Group groupExist = groupRepository.findByName(user.getGroupName());
        if(userExist!=null){
            return new ResponseEntity("Username/password already existed!", HttpStatus.BAD_REQUEST);
        }
        if (groupExist == null){
            user.setGroupName("GROUPLESS"); // terakhir sampai disini
//            return new ResponseEntity<>("Failed to save User!\nGroup doesn't exists!", HttpStatus.BAD_REQUEST);
        }
        if (user.getRole().equals("GROUP_ADMIN")){
            if(groupExist.getGroupAdmin()!=null)
                return new ResponseEntity<>("Failed to save User!\nGroup admin already exists!", HttpStatus.BAD_REQUEST);
            else{
                groupExist.setGroupAdmin(user.getEmail());
                groupRepository.save(groupExist);// save perubahan admin pada group
            }
        }

        if(checkImageFile(file)){
            try{
                String fileName="user/"+user.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                user.setImagePath(fileName);
                user.setImageURL(UPLOADED_URL+fileName);
            }catch (IOException e){
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        if(user.getTotalPeriodPayed()==null){
            user.setTotalPeriodPayed(0);
        }
        user.setTotalPeriodPayed(groupExist.getCurrentPeriod()+user.getTotalPeriodPayed()-1);//-1 karena bulan sekarang
        user.setPeriodeTertinggal(groupExist.getCurrentPeriod()-user.getTotalPeriodPayed());
        user.setJoinDate(new Date().getTime());
        user.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        user.setActive(true);
        user.setBalance((user.getTotalPeriodPayed()-groupExist.getCurrentPeriod()+1)*groupExist.getRegularPayment());
        user.setBalanceUsed((double)0);

        userRepository.save(user);
        notifMessage= user.getName()+USER_JOINED_GROUP;
        notificationService.createNotification(notifMessage,user.getEmail(),user.getGroupName(),TYPE_GROUP);

        return new ResponseEntity<>("Succeed to create user!",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteUser(String email) {
        User userExist = userRepository.findByEmail(email);
        if (userExist == null)
            return new ResponseEntity<>("Failed to delete User!\nUserId not found!", HttpStatus.BAD_REQUEST);

        userExist.setActive(false);
        userRepository.save(userExist);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }


}

