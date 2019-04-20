package com.future.tcfm.model;

import com.future.tcfm.model.detail.PaymentDetail;
import com.future.tcfm.model.list.PeriodPayed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Period;
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
    private String idGroup;
    private String rekening;
    private double balance;
    private List<PaymentDetail> paymentDetail;
    private List<PeriodPayed> periodPayed;
}
