package com.future.tcfm.model.detail;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class PaymentDetail {
    @Id
    private String idPayment;
    private String idUser;
    private String idGroup;
    private Date paymentDate;
}
