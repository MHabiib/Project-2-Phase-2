package com.future.tcfm.model;

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
@Document(collection = "payment")
public class Payment {
    @Id
    private String idPayment;
    private String email;
    private String groupName;
    private Long paymentDate;
    private Long lastModifiedAt;
    private Double price;
    private Boolean isPaid;
    private String imagePath;
    private String imageURL;
    private List<PaymentDetail> paymentDetail;
}
