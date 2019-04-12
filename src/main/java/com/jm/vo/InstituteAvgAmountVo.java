package com.jm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstituteAvgAmountVo {
	
	private int year;

    private String instituteName;
    
    private long avgAmount;
}
