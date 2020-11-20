package com.jm.model.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceStatVo {

    private int year;
    
    private long totalAmount;
    
    private String instituteName;

    private Map<String, Long> detailAmount;
    
    public FinanceStatVo(int year, long totalAmount) {
    	this.year = year;
    	this.totalAmount = totalAmount;
    }
}
