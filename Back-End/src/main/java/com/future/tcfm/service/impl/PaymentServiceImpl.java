package com.future.tcfm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.Payment;
import com.future.tcfm.model.ReqResModel.ExpenseRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.PaymentRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.NotificationService;
import com.future.tcfm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;
import static com.future.tcfm.service.impl.NotificationServiceImpl.PAYMENT_APPROVED_MESSAGE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.PAYMENT_MESSAGE;
import static com.future.tcfm.service.impl.NotificationServiceImpl.PAYMENT_REJECTED_MESSAGE;
import static com.future.tcfm.service.impl.UserServiceImpl.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserRepository userRepository;

    String notificationMessage;
    @Override
    public ResponseEntity createPayment(String paymentJSONString, MultipartFile file) throws IOException {
        Payment payment  = new ObjectMapper().readValue(paymentJSONString, Payment.class);
        Group groupExist = groupRepository.findByName(payment.getGroupName());
        User userExist = userRepository.findByIdUser(payment.getEmail());
        if(payment==null || payment.getEmail() == null || payment.getGroupName() == null){
            return new ResponseEntity("400: Payment is null", HttpStatus.BAD_REQUEST);
        }
        if(userExist==null){
            return new ResponseEntity("User email does not exist", HttpStatus.NOT_FOUND);
        }
        if(groupExist==null){
            return new ResponseEntity("Group name does not exist", HttpStatus.NOT_FOUND);
        }
        if(checkImageFile(file)){
            try{
                String fileName=String.valueOf(System.currentTimeMillis())+"_"+payment.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                payment.setImagePath(fileName);
                payment.setImageURL(UPLOADED_URL+fileName);
            }catch (IOException e){
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setGroupName(payment.getGroupName());
        payment.setLastModifiedAt(System.currentTimeMillis());
        paymentRepository.save(payment);
        notificationMessage = payment.getEmail()+ PAYMENT_MESSAGE; //getCurrentUser() = get current logged in user
        notificationService.createNotification(notificationMessage,getCurrentUser().getEmail(),getCurrentUser().getGroupName());
        return new ResponseEntity<>("Succeed to create payment!",HttpStatus.OK);
    }

    @Override
    public ResponseEntity updatePayment(String id, String paymentJSONString, MultipartFile file) throws IOException {
        Payment payment = new ObjectMapper().readValue(paymentJSONString, Payment.class);
        Payment paymentExist = paymentRepository.findByIdPayment(id);
        if (paymentExist == null) {
            return new ResponseEntity("Payment not found!", HttpStatus.NOT_FOUND);
        }
        if (checkImageFile(file)) {
            try {
                if (paymentExist.getImagePath() != null) {
                    Path deletePath = Paths.get(UPLOADED_FOLDER + paymentExist.getImagePath());
                    Files.delete(deletePath);
                }
                String fileName=String.valueOf(System.currentTimeMillis())+"_"+payment.getEmail()+"_"+file.getOriginalFilename();
                saveUploadedFile(file, fileName);
                paymentExist.setImagePath(fileName);
                paymentExist.setImageURL(UPLOADED_URL + fileName);
            } catch (IOException e) {
                return new ResponseEntity<>("Some error occured. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        paymentExist.setPrice(payment.getPrice());
        paymentExist.setLastModifiedAt(System.currentTimeMillis());
        paymentExist.setPaymentDetail(payment.getPaymentDetail());

        paymentRepository.save(payment);
        return new ResponseEntity(paymentExist, HttpStatus.OK);
    }


    @Override
    public ResponseEntity managementPayment(ExpenseRequest thisPayment) {
        Payment paymentExist = paymentRepository.findByIdPayment(thisPayment.getId());
        if(paymentExist==null){
            return new ResponseEntity("Payment not found!",HttpStatus.NOT_FOUND);
        }
        if(thisPayment.getStatus()){
            paymentExist.setIsPaid(true);
            notificationMessage = paymentExist.getEmail()+ PAYMENT_APPROVED_MESSAGE + getCurrentUser().getEmail(); //getCurrentUser() = get current logged in user
        }
        else if(!thisPayment.getStatus()){
            paymentExist.setIsPaid(false);
            notificationMessage = paymentExist.getEmail()+ PAYMENT_REJECTED_MESSAGE + getCurrentUser().getEmail(); //getCurrentUser() = get current logged in user
        }
        notificationService.createNotification(notificationMessage,getCurrentUser().getEmail(),getCurrentUser().getGroupName());

        paymentExist.setLastModifiedAt(System.currentTimeMillis());
        paymentRepository.save(paymentExist);
        return new ResponseEntity(paymentExist,HttpStatus.OK);
    }

    @Override
    public ResponseEntity findAll() {
        return null;
    }

    @Override
    public ResponseEntity findById() {
        return null;
    }

    @Override
    public ResponseEntity findByIdUser() {
        return null;
    }

    @Override
    public ResponseEntity findByGroupName(String groupName) {
        List<Payment> paymentList = paymentRepository.findAllByGroupNameOrderByLastModifiedAt(groupName);
        if(paymentList==null) return new ResponseEntity("Error: 404 Not Found",HttpStatus.NOT_FOUND);
        return new ResponseEntity(paymentList,HttpStatus.OK);
    }
}
