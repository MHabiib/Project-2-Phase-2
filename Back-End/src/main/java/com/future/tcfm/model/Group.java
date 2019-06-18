package com.future.tcfm.model;

import com.future.tcfm.model.list.Members;
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
    private Double groupBalance;
    private Double totalExpense;
    private Boolean active;
}
