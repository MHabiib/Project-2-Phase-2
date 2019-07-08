package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.future.tcfm.service.impl.NotificationServiceImpl.EXPENSE_APPROVED_MESSAGE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.USER_JOINED_GROUP;
import static com.future.tcfm.service.impl.NotificationServiceImpl.USER_LEFT_GROUP;

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

    private String notifMessage;
    @Override
    public List<User> loadAll() {
        return userRepository.findAll();
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
                String fileName=userExist.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                userExist.setImagePath(fileName);
                userExist.setImageURL(UPLOADED_URL+fileName);
            }catch (IOException e){
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        if(userExist.getGroupName()!=user.getGroupName()) {
            //notification untuk group lamanya
            notifMessage = userExist.getEmail()+USER_LEFT_GROUP;
            notificationService.createNotification(notifMessage,userExist.getEmail(),userExist.getGroupName());
            //notification untuk group barunya
            notifMessage = userExist.getEmail()+USER_JOINED_GROUP;
            notificationService.createNotification(notifMessage,userExist.getEmail(),user.getGroupName());
            userExist.setJoinDate(new Date().getTime());
            userExist.setGroupName(user.getGroupName());
        }
        userExist.setEmail(user.getEmail());
        userExist.setName(user.getName());
        userExist.setPhone(user.getPhone());
        userExist.setPassword(user.getPassword());
        userExist.setRole(user.getRole());
        userExist.setBalance(user.getBalance());
        userExist.setPeriodPayed(user.getPeriodPayed());
        userExist.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        userRepository.save(userExist);

        return new ResponseEntity(userExist,HttpStatus.OK);
    }


    @Override
    public ResponseEntity createUserV2(String userJSONString, MultipartFile file) throws IOException {
        User user  = new ObjectMapper().readValue(userJSONString, User.class);
        User userExist = userRepository.findByEmail(user.getEmail());
        Group groupExist = groupRepository.findByName(user.getGroupName());
        if(userExist!=null){
            return new ResponseEntity("Username/password already existed!", HttpStatus.BAD_REQUEST);
        }
        if (groupExist == null){
            return new ResponseEntity<>("Failed to save User!\nGroup doesn't exists!", HttpStatus.BAD_REQUEST);
        }
        if(checkImageFile(file)){
            try{
                String fileName=user.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                user.setImagePath(fileName);
                user.setImageURL(UPLOADED_URL+fileName);
            }catch (IOException e){
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        user.setJoinDate(new Date().getTime());
        user.setPassword(passwordEncoder.encode(user.getPassword()));//ENCRYPTION PASSWORD
        user.setActive(true);
        userRepository.save(user);
        return new ResponseEntity<>("Succeed to create user!",HttpStatus.OK);
    }



}

/*    @Override
    public ResponseEntity<?> deleteUser(String id) {
        User userExist = userRepository.findByIdUser(id);
        if (userExist == null)
            return new ResponseEntity<>("Failed to delete User!\nUserId not found!", HttpStatus.BAD_REQUEST);
        userExist.setActive(false);
        userRepository.save(userExist);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }*/
