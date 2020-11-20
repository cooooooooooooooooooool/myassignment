package com.jm.model.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstituteAvgMinMaxAmountVo {

    private String instituteName;
    
    private List<Map<Integer, Long>> supportAmount;
}
