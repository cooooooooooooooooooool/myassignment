package com.myassign.controller;

import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myassign.model.dto.TransactionDto;
import com.myassign.model.dto.TransactionResultDto;
import com.myassign.model.entity.Transaction;
import com.myassign.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 뿌리기 처리 후 토큰 반환
     */
    @PostMapping
    public String spray(@RequestHeader("X-USER-ID") String userId, @RequestHeader("X-ROOM-ID") UUID roomId, @RequestParam int totalPrice, @RequestParam int userCount) {
        return transactionService.sprayPrice(userId, roomId, totalPrice, userCount);
    }

    /**
     * 받기
     */
    @PutMapping("/{token}")
    public TransactionResultDto receive(@RequestHeader("X-USER-ID") String userId, @RequestHeader("X-ROOM-ID") UUID roomId, @PathVariable("token") String token) {
        return transactionService.receivePrice(userId, roomId, token);
    }

    /**
     * 뿌리기 처리 후 토큰 반환
     */
    @GetMapping
    public TransactionDto getTransaction(@RequestHeader("X-ROOM-ID") UUID roomId, @RequestParam String token) {
        return convert(transactionService.getTransaction(roomId, token));
    }

    private TransactionDto convert(Transaction transaction) {

        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);

        if (transaction.getTransactionUserList() != null) {
            transactionDto.setTransactionUsers(transaction.getTransactionUserList().stream().map(e -> modelMapper.map(e, TransactionResultDto.class)).collect(Collectors.toList()));
        }

        log.info("transactionDto : {}", transactionDto);

        return transactionDto;
    }
}