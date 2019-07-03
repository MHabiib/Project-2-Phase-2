package com.future.tcfm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "overview")
public class Overview {
    private List<Expense> latestExpense;
    private Double groupBalance;
    private Integer totalMembers;
    private Long latestJoinDate;
    private Long latestExpenseDate;
}
