package com.future.tcfm.model.detail;

import com.future.tcfm.model.list.PaymentDetail;
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
@Document(collection = "payment_detail")
public class Payment {
    @Id
    private String idPayment;
    private String idUser;
    private String idGroup;
    private Long paymentDate;
    private Boolean isPaid;
    private List<PaymentDetail> paymentDetail;
}
