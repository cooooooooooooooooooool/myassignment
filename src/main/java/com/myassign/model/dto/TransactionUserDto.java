package com.myassign.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUserDto {

    private Long id;

    private TransactionDto transaction;

    private String receiveUserId;

    private Integer order;

    private Long price;

    private Date receiveDate;

    private Date createDate;
}
