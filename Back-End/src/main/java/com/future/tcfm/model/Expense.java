package com.future.tcfm.model;

import com.future.tcfm.model.list.ApproverContributor;
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
    private List<ApproverContributor>contributorList;
    private List<ApproverContributor>approverList;
    private Long createdDate;
    private Long rejectedDate;
    private Long approvedDate;
    private Boolean status;
    private Integer percentageApproved;

    public void setContributorList(List<ApproverContributor> contributorList) {
        this.contributorList = contributorList;
    }

    public void setApproverList(List<ApproverContributor> approverList) {
        this.approverList = approverList;
    }

}
