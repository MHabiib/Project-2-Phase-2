package com.future.tcfm.model;

import com.future.tcfm.model.list.Approver;
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
    private String detail;
    private Double price;
    private List<User>contributorList;
    private Long date;
    private List<User>approverList;
}
