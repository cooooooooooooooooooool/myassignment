package com.myassign.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.myassign.model.entity.Room;
import com.myassign.model.entity.User;
import com.myassign.service.RoomService;
import com.myassign.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    private final UserService userService;
    private final RoomService roomService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<User> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            /* @formatter:off */
            User user = User.builder()
                            .id("user-"+i)
                            .name("전우치"+i)
                            .password(DigestUtils.sha256Hex("1234"))
                            .balance(Long.valueOf(Integer.MAX_VALUE))
                            .createDate(new Date()).build();
            /* @formatter:on */
            log.info("user : " + user);
            list.add(user);
        }

        userService.createUser(list);

        Room room = roomService.createRoom("my chatting room");

        if (!list.isEmpty()) {
            list.forEach(user -> {
                roomService.joinRoomUser(room.getId(), user.getId());
            });
        }
    }
}
