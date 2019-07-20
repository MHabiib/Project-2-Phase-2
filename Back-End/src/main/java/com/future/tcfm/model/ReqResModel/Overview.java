package com.future.tcfm.model.ReqResModel;

import com.future.tcfm.model.Expense;
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
public class Overview {
    private List<Expense> latestExpense;
    private Double groupBalance;
    private Integer totalMembers;
    private Integer paymentPaidThisMonth;
    private double percentageTotalCashUsed;
    private Long latestJoinDate;
    private Long latestExpenseDate;
}
