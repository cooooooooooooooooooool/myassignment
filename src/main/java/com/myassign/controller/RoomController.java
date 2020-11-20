package com.myassign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myassign.model.entity.Room;
import com.myassign.service.RoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 대화방 ID 를 조회
     */
    @GetMapping
    public String getRoomId() {
        Room room = roomService.getTopRoom();

        log.info("room : {}", room.getId());
        return room.getId().toString();
    }
}