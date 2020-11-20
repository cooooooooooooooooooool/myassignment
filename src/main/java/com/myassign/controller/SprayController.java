package com.myassign.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myassign.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/spray")
public class SprayController {

    @Autowired
    private TransactionService sprayService;

    /**
     * 뿌리기 처리 후 토큰 반환
     */
    @PostMapping
    public String spray(@RequestHeader("X-USER-ID") String userId, @RequestHeader("X-ROOM-ID") UUID roomId, @RequestParam int totalPrice, @RequestParam int userCount) {
        log.info("userId : {}, roomId : {}", userId, roomId);
        return sprayService.sprayPrice(userId, roomId, totalPrice, userCount);
    }
}