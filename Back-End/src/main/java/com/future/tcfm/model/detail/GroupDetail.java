package com.future.tcfm.model.detail;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class GroupDetail {
    @Id
    private String idGroupDetail;
    private String idUser;
    private String idGroup;
    private String role;
    private Double balance;
    private Integer totalPayment;
    private Long joinDate;
    private Integer join; //join pada reccuring keberapa
}
