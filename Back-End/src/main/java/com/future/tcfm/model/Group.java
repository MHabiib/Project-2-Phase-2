package com.future.tcfm.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private String groupAdmin;
    private Double regularPayment;
    private Long createdDate;
    private Long closedDate;
    private Double groupBalance;
    private Double balanceUsed;
    private Double totalExpense;
    private Boolean active;

    @JsonIgnore
    public Integer getCurrentPeriod(){
        return  new Date(System.currentTimeMillis()).toLocalDate().getMonthValue()- new Date(createdDate).toLocalDate().getMonthValue();
    }
}
