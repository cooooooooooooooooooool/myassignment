package com.myassign.model.dto;

import java.util.Date;
import java.util.List;

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
public class TransactionStatusDto {

    private Date sprayDate;

    private Long sprayPrice;

    private Long receiveFinishPrice;

    private List<TransactionReceiveUserDto> receiverUsers;
}