package com.future.tcfm.model;

import com.future.tcfm.model.list.PaymentDetail;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

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
    private String groupName;
    private String rekening;
    private Double balance;
    private List<PaymentDetail> periodPayed;
    private String imagePath;
    private String imageURL;
    public void setPeriodPayed(List<PaymentDetail> periodPayed) {
        this.periodPayed = periodPayed;
    }
}
