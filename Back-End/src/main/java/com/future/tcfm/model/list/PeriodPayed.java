package com.future.tcfm.model.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PeriodPayed {
    private String month;
    private String year;
    private Boolean payed;
}
