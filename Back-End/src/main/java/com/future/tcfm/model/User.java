package com.future.tcfm.model;

import com.future.tcfm.model.detail.GroupDetail;
import com.future.tcfm.model.detail.PaymentDetail;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data

@Document(collection = "user")
public class User {
    @Id
    private String idUser;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Boolean superAdmin;
    private List<GroupDetail> groupDtl;
    private List<PaymentDetail>paymentDtl;
}
