package com.future.tcfm.model.detail;

import com.future.tcfm.model.list.PeriodPayed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_detail")
public class PaymentDetail {
    @Id
    private String idPayment;
    private String idUser;
    private String idGroup;
    private Long paymentDate;
    private List<PeriodPayed> periodPayed;
}
