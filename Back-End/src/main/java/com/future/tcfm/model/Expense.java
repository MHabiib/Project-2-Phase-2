package com.future.tcfm.model;

import com.future.tcfm.model.list.Approver;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.future.tcfm.model.list.Contributor;

import java.util.Date;
import java.util.List;

@Data

@Document(collection = "expense")
public class Expense {
    @Id
    private String idExpense;
    private String detail;
    private Double price;
    private List<Contributor>contributorList;
    private Date date;
    private List<Approver>approverList;
}
