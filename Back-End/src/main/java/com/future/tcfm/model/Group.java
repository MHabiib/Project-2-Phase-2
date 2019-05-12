package com.future.tcfm.model;

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
@Document(collection = "group")
public class Group {
    @Id
    private String idGroup;
    private String name;
    private Double regularPayment;
    private Long createdDate;
    private Long closedDate;
    private double groupBalance;
    private List<Expense>expenseList;
    private List<User> member;
//    private List<PaymentDetail> paymentDetail;
    private Boolean active;
}
