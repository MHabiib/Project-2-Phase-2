package com.future.tcfm.model;

import com.future.tcfm.model.detail.GroupDetail;
import com.future.tcfm.model.detail.PaymentDetail;
import com.future.tcfm.model.Expense;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data

@Document(collection = "group")
public class Group {
    @Id
    private String idGroup;
    private String name;
    private Double regularPayment;
    private Integer recurring; //date / integer?
    private Date createdDate;
    private Date closedDate;
    private List<GroupDetail> groupDtl;
    private List<PaymentDetail>paymentDtl;
    private List<Expense>expenseList;
}
