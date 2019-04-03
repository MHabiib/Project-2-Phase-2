package com.future.tcfm.model.detail;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PaymentDetail {
    @Id
    private String idPayment;
    private String idUser;
    private String idGroup;
    private Long paymentDate;
    private List<Map> periodPayed;
}
