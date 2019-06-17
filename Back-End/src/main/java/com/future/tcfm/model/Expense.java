package com.future.tcfm.model;

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
@Document(collection = "expense")
public class Expense {
    @Id
    private String idExpense;
    private String groupName;
    private String title;
    private String detail;
    private Double price;
    private List<User>contributorList;
    private List<User>approverList;
    private Long createdDate;
    private Long rejectedDate;
    private Long approvedDate;
    private Boolean status;

    public void setDate(Object o) {
    }
}
