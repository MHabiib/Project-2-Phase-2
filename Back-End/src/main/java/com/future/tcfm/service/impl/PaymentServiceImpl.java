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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.OutputKeys;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.future.tcfm.config.SecurityConfig.getCurrentUser;
import static com.future.tcfm.service.impl.ExpenseServiceImpl.createPageRequest;
import static com.future.tcfm.service.impl.NotificationServiceImpl.*;
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
        System.out.print("Isi payment:");
        System.out.print(payment);
        User userExist = userRepository.findByEmail(payment.getEmail());
        Group groupExist = groupRepository.findByName(userExist.getGroupName());

        if(payment.getEmail() == null){
            return new ResponseEntity("400: Payment is null", HttpStatus.BAD_REQUEST);
        }
        if(userExist==null){
            return new ResponseEntity("User email does not exist", HttpStatus.NOT_FOUND);
        }
        if(groupExist==null) {
            return new ResponseEntity("Group name does not exist", HttpStatus.NOT_FOUND);
        }
        if(checkImageFile(file)){
            try {
//              Ini String.valueOf() nya gw delete soalnya kata SpringBoot itu not necessary. Kalau ternyata perlu masukin lagi + kabarin y
                String fileName = System.currentTimeMillis() + "_" + payment.getEmail() + "_" + file.getOriginalFilename();
                saveUploadedFile(file,fileName);
                payment.setImagePath(fileName);
                payment.setImageURL(UPLOADED_URL+fileName);
            } catch (IOException e){
                return new ResponseEntity<>("Some error occurred. Failed to add image", HttpStatus.BAD_REQUEST);
            }
        }
        payment.setIsChecked(false);
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setGroupName(userExist.getGroupName());
        payment.setLastModifiedAt(System.currentTimeMillis());
        paymentRepository.save(payment);
        notificationMessage = payment.getEmail()+ PAYMENT_MESSAGE; //getCurrentUser() = get current logged in user
        notificationService.createNotification(notificationMessage,getCurrentUser().getEmail(),getCurrentUser().getGroupName(),TYPE_PERSONAL);
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
        paymentExist.setIsRejected(null);
        paymentExist.setPrice(payment.getPrice());
        paymentExist.setLastModifiedAt(System.currentTimeMillis());
        paymentExist.setPeriode(payment.getPeriode());

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
            if(!paymentExist.getIsChecked()){
                paymentExist.setIsRejected(false);
                User user = userRepository.findByEmail(paymentExist.getEmail());
                Group group = groupRepository.findByName(paymentExist.getGroupName());

                user.setBalance(user.getBalance()+paymentExist.getPrice());
                user.setTotalPeriodPayed(user.getTotalPeriodPayed()+paymentExist.getPeriode());
                user.setPeriodeTertinggal(group.getCurrentPeriod()-user.getTotalPeriodPayed());// jika minus bearti user surplus
                userRepository.save(user);

                group.setGroupBalance(group.getGroupBalance()+paymentExist.getPrice());
                groupRepository.save(group);

                notificationMessage = paymentExist.getEmail()+ PAYMENT_APPROVED_MESSAGE + paymentExist.getEmail(); //getCurrentUser() = get current logged in user
            }
        }
        else {
            paymentExist.setIsRejected(true);
            notificationMessage = paymentExist.getEmail()+ PAYMENT_REJECTED_MESSAGE + paymentExist.getEmail(); //getCurrentUser() = get current logged in user
        }
        paymentExist.setIsChecked(true);
        paymentExist.setLastModifiedAt(System.currentTimeMillis());
        paymentRepository.save(paymentExist);
        notificationService.createNotification(notificationMessage,getCurrentUser().getEmail(),paymentExist.getEmail(),TYPE_PERSONAL);

        return new ResponseEntity(paymentExist,HttpStatus.OK);
    }


    @Override
    public ResponseEntity findAll() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @Override
    public ResponseEntity findById(String id) {
        Payment paymentExist = paymentRepository.findByIdPayment(id);
        if(paymentExist==null){
            return new ResponseEntity("\"\\\"{\\\"error\\\":\\\"404 not found\\\"\"",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(paymentExist,HttpStatus.OK);

    }

    @Override
    public ResponseEntity findByEmail(String email,String filter,int page, int size) {
        Page<Payment> paymentExist = paymentRepository.findAllByEmailOrderByLastModifiedAt(email,createPageRequest(filter,"desc",page,size));
        if(paymentExist==null){
            return new ResponseEntity("\"\\\"{\\\"error\\\":\\\"404 not found\\\"\"",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(paymentExist,HttpStatus.OK);

    }

    @Override
    public ResponseEntity findByGroupNameAndIsPaid(String groupName,String filter, int page, int size) {
        Page<Payment> paymentList = paymentRepository.findAllByGroupNameOrderByPaymentDateDesc(groupName,createPageRequest(filter,"desc",page,size));
        if(paymentList==null) return new ResponseEntity<>("Error: 404 Not Found",HttpStatus.NOT_FOUND);
        return new ResponseEntity(paymentList,HttpStatus.OK);
    }



    /**
     * ambil total berapa persen sudah payment yang diterima dalam bulan X
     * @return
     */

}
