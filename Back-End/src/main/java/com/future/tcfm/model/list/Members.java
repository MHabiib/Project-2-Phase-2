package com.future.tcfm.model.list;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Members {
    private String email;

    public Members(){}

    public Members (String email){
        this.email=email;
    }

}
