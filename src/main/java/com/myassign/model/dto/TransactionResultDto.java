package com.myassign.model.dto;

import java.util.Date;
import java.util.UUID;

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
public class TransactionResultDto {

    private UUID roomId;

    private String roomName;

    private String token;

    private Long price;

    private Date receiveDate;
}
