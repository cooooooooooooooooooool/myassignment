package com.myassign.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myassign.model.dto.TransactionResultDto;
import com.myassign.model.dto.TransactionStatusDto;
import com.myassign.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final TransactionService transactionService;

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
     * 뿌리기 상태를 조회
     */
    @GetMapping
    public TransactionStatusDto getTransactionStatus(@RequestHeader("X-USER-ID") String userId, @RequestHeader("X-ROOM-ID") UUID roomId, @RequestParam String token) {
        return transactionService.getTransactionStatus(userId, roomId, token);
    }
}