package com.future.tcfm.model;

import com.future.tcfm.model.detail.GroupDetail;
import com.future.tcfm.model.detail.PaymentDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String idUser;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Long joinDate;
    private Long leaveDate;
    private Boolean active;
    private String idGroup;
    private String rekening;
    private double balance;

}
