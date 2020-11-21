package com.myassign.model.dto;

import java.util.Date;
import java.util.List;
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
public class TransactionDto {

    private UUID id;

    private RoomDto room;

    private String token;

    private UserDto spreadUser;

    private Long totalPrice;

    private List<TransactionResultDto> transactionUsers;

    private Date createDate;
}