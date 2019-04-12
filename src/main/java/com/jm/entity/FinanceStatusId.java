package com.jm.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceStatusId implements Serializable {
	
	private static final long serialVersionUID = 2047933882727173365L;

	@Id
    private int year;
    
    @Id
    private int month;
    
    @Id
    private String code;
}